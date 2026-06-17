package com.banking.smartbank.transaction.event;

import com.banking.smartbank.transaction.model.Transaction;
import com.banking.smartbank.transaction.repository.TransactionRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Component
public class ReversalConsumer {
    private final TransactionRepository txnRepo;
    private final RestTemplate rest;

    public ReversalConsumer(TransactionRepository r) {
        this.txnRepo = r;
        this.rest = new RestTemplate();
    }

    @Transactional
    @KafkaListener(topics = "reversal-events", groupId = "reconciler-group")
    public void processReversal(TransactionEvent event) {
        Transaction txn = txnRepo.findById(event.getTransactionId()).orElse(null);
        if (txn == null || !"PENDING_REVERSAL".equals(txn.getStatus())) return;

        try {
            Double toBalance = rest.getForObject(
                "http://account-service:8082/accounts/" + event.getToAccountId(), java.util.Map.class)
                .get("balance") instanceof Double d ? d : 0.0;

            boolean creditApplied = rest.getForObject(
                "http://account-service:8082/accounts/" + event.getToAccountId() + "/transactions?ref=TXN-" + event.getTransactionId(),
                Boolean.class);

            if (Boolean.TRUE.equals(creditApplied)) {
                txn.setStatus("COMPLETED");
            } else {
                rest.put("http://account-service:8082/accounts/" + event.getFromAccountId() + "/balance?delta=" + event.getAmount(), null);
                txn.setStatus("REVERSED");
            }
            txnRepo.save(txn);
        } catch (Exception e) {
            // Will retry on next Kafka rebalance
            throw new RuntimeException("Reversal failed, will retry", e);
        }
    }
}
