package com.banking.smartbank.audit.event;

import com.banking.smartbank.audit.service.AuditService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AuditEventConsumer {
    private final AuditService auditService;

    public AuditEventConsumer(AuditService s) { this.auditService = s; }

    @KafkaListener(topics = "transfer-events", groupId = "audit-group")
    public void consume(TransactionEvent event) {
        String details = String.format("Transfer %.2f from account %d to %d — %s",
            event.getAmount(), event.getFromAccountId(), event.getToAccountId(), event.getStatus());
        auditService.log(event.getType(), event.getUserEmail(), details, "transaction-service");
    }
}
