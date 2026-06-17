package com.banking.smartbank.transaction.service;

import com.banking.smartbank.transaction.model.Transaction;
import com.banking.smartbank.transaction.repository.TransactionRepository;
import com.banking.smartbank.transaction.event.TransactionEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {
    private final TransactionRepository txnRepo;
    private final TransactionEventPublisher publisher;
    private final RestTemplate rest;

    public TransactionService(TransactionRepository tr, TransactionEventPublisher p) {
        this.txnRepo = tr;
        this.publisher = p;
        this.rest = new RestTemplate();
    }

    @Transactional
    public Transaction transfer(Long fromAccountId, Long toAccountId, Double amount, String idempotencyKey) {
        if (idempotencyKey != null && txnRepo.findByIdempotencyKey(idempotencyKey).isPresent()) {
            return txnRepo.findByIdempotencyKey(idempotencyKey).get();
        }

        Double fromBalance = rest.getForObject(
            "http://account-service:8082/accounts/" + fromAccountId, Map.class)
            .get("balance") instanceof Double d ? d : 0.0;

        if (fromBalance < amount) {
            throw new RuntimeException("Insufficient funds");
        }

        rest.put("http://account-service:8082/accounts/" + fromAccountId + "/balance?delta=" + (-amount), null);

        try {
            rest.put("http://account-service:8082/accounts/" + toAccountId + "/balance?delta=" + amount, null);
        } catch (Exception e) {
            Transaction pending = new Transaction();
            pending.setFromAccountId(fromAccountId); pending.setToAccountId(toAccountId);
            pending.setAmount(amount); pending.setType("TRANSFER"); pending.setStatus("PENDING_REVERSAL");
            pending.setIdempotencyKey(idempotencyKey);
            Transaction saved = txnRepo.save(pending);
            publisher.publishPendingReversal(saved.getId(), fromAccountId, toAccountId, amount);
            return saved;
        }

        Transaction txn = new Transaction();
        txn.setFromAccountId(fromAccountId); txn.setToAccountId(toAccountId);
        txn.setAmount(amount); txn.setType("TRANSFER"); txn.setStatus("COMPLETED");
        txn.setIdempotencyKey(idempotencyKey);
        Transaction saved = txnRepo.save(txn);
        publisher.publish(saved, "system");
        return saved;
    }

    public List<Transaction> getAccountTransactions(Long accountId) {
        return txnRepo.findByFromAccountIdOrToAccountIdOrderByTimestampDesc(accountId, accountId);
    }
}
