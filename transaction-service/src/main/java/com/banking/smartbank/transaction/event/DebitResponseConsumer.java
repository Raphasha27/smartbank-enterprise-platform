package com.banking.smartbank.transaction.event;

import com.banking.smartbank.transaction.service.TransactionService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DebitResponseConsumer {
    private final TransactionService transactionService;

    public DebitResponseConsumer(TransactionService s) { this.transactionService = s; }

    @KafkaListener(topics = "debit-responses", groupId = "transaction-group")
    public void consume(DebitResponse response) {
        transactionService.completePendingResponse(response);
    }
}
