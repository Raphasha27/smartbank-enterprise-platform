package com.banking.smartbank.transaction.event;

public class DebitRequest {
    private String correlationId;
    private Long accountId;
    private Double amount;
    private String operation;
    private Long version;

    public DebitRequest() {}

    public DebitRequest(String correlationId, Long accountId, Double amount, String operation, Long version) {
        this.correlationId = correlationId; this.accountId = accountId;
        this.amount = amount; this.operation = operation; this.version = version;
    }

    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String v) { this.correlationId = v; }
    public Long getAccountId() { return accountId; }
    public void setAccountId(Long v) { this.accountId = v; }
    public Double getAmount() { return amount; }
    public void setAmount(Double v) { this.amount = v; }
    public String getOperation() { return operation; }
    public void setOperation(String v) { this.operation = v; }
    public Long getVersion() { return version; }
    public void setVersion(Long v) { this.version = v; }
}
