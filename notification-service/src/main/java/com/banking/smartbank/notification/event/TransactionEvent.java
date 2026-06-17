package com.banking.smartbank.notification.event;

import java.time.LocalDateTime;

public class TransactionEvent {
    private Long transactionId;
    private Long fromAccountId;
    private Long toAccountId;
    private Double amount;
    private String type;
    private String status;
    private String userEmail;
    private LocalDateTime timestamp;

    public TransactionEvent() {}

    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long v) { this.transactionId = v; }
    public Long getFromAccountId() { return fromAccountId; }
    public void setFromAccountId(Long v) { this.fromAccountId = v; }
    public Long getToAccountId() { return toAccountId; }
    public void setToAccountId(Long v) { this.toAccountId = v; }
    public Double getAmount() { return amount; }
    public void setAmount(Double v) { this.amount = v; }
    public String getType() { return type; }
    public void setType(String v) { this.type = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String v) { this.userEmail = v; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime v) { this.timestamp = v; }
}
