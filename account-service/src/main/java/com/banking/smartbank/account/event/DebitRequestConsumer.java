package com.banking.smartbank.account.event;

import com.banking.smartbank.account.model.Account;
import com.banking.smartbank.account.repository.AccountRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DebitRequestConsumer {
    private final AccountRepository repo;
    private final KafkaTemplate<String, DebitResponse> kafka;

    public DebitRequestConsumer(AccountRepository r, KafkaTemplate<String, DebitResponse> k) {
        this.repo = r; this.kafka = k;
    }

    @Transactional
    @KafkaListener(topics = "debit-requests", groupId = "account-group")
    public void handle(DebitRequest req) {
        try {
            Account acc = repo.findById(req.getAccountId()).orElse(null);
            if (acc == null) {
                sendResponse(req.getCorrelationId(), req.getAccountId(), 0.0, 0L, false, "Account not found");
                return;
            }

            boolean success;
            if ("DEBIT".equals(req.getOperation())) {
                int updated = repo.atomicDebit(req.getAccountId(), req.getAmount(), req.getVersion());
                success = updated > 0;
            } else {
                int updated = repo.atomicCredit(req.getAccountId(), req.getAmount());
                success = updated > 0;
            }

            if (!success) {
                sendResponse(req.getCorrelationId(), req.getAccountId(), acc.getBalance(), acc.getVersion(), false, "Optimistic lock failure or insufficient funds");
                return;
            }

            Account updated = repo.findById(req.getAccountId()).get();
            sendResponse(req.getCorrelationId(), req.getAccountId(), updated.getBalance(), updated.getVersion(), true, null);
        } catch (Exception e) {
            sendResponse(req.getCorrelationId(), req.getAccountId(), 0.0, 0L, false, e.getMessage());
        }
    }

    private void sendResponse(String correlationId, Long accountId, Double balance, Long version, boolean success, String reason) {
        kafka.send("debit-responses", correlationId,
            new DebitResponse(correlationId, accountId, balance, version, success, reason));
    }
}
