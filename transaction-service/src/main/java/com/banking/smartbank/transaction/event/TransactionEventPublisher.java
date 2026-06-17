package com.banking.smartbank.transaction.event;

import com.banking.smartbank.transaction.model.Transaction;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventPublisher {
    private final KafkaTemplate<String, TransactionEvent> kafka;

    public TransactionEventPublisher(KafkaTemplate<String, TransactionEvent> k) { this.kafka = k; }

    public void publish(Transaction txn, String userEmail) {
        TransactionEvent event = new TransactionEvent(
            txn.getId(), txn.getFromAccountId(), txn.getToAccountId(),
            txn.getAmount(), txn.getType(), txn.getStatus(), userEmail);
        kafka.send("transfer-events", String.valueOf(txn.getFromAccountId()), event);
    }

    public void publishPendingReversal(Long txnId, Long fromAccountId, Long toAccountId, Double amount) {
        TransactionEvent event = new TransactionEvent(txnId, fromAccountId, toAccountId, amount, "TRANSFER", "PENDING_REVERSAL", null);
        kafka.send("reversal-events", String.valueOf(fromAccountId), event);
    }

    public void publishDebitRequest(com.banking.smartbank.transaction.event.DebitRequest req) {
        kafka.send("debit-requests", String.valueOf(req.getAccountId()), req);
    }
}
