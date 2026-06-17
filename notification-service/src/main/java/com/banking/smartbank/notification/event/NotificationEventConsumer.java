package com.banking.smartbank.notification.event;

import com.banking.smartbank.notification.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventConsumer {
    private final NotificationService notificationService;

    public NotificationEventConsumer(NotificationService s) { this.notificationService = s; }

    @KafkaListener(topics = "transfer-events", groupId = "notification-group")
    public void consume(TransactionEvent event) {
        if (event.getUserEmail() != null) {
            String msg = String.format("Transfer of %.2f completed. Ref: TXN-%d", event.getAmount(), event.getTransactionId());
            notificationService.send(event.getUserEmail(), "TRANSFER", msg);
        }
    }
}
