package com.banking.smartbank.ledger.event;

import com.banking.smartbank.ledger.service.LedgerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class LedgerEventConsumer {
    private final LedgerService ledgerService;

    public LedgerEventConsumer(LedgerService s) { this.ledgerService = s; }

    @KafkaListener(topics = "transfer-events", groupId = "ledger-group")
    public void consume(TransactionEvent event) {
        if ("COMPLETED".equals(event.getStatus())) {
            ledgerService.recordTransfer(
                "TXN-" + event.getTransactionId(),
                event.getFromAccountId(),
                event.getToAccountId(),
                event.getAmount(),
                "ZAR");
        }
    }
}
