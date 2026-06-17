package com.banking.smartbank.account.event;

public class DebitResponse {
    private String correlationId;
    private Long accountId;
    private Double balance;
    private Long version;
    private boolean success;
    private String reason;

    public DebitResponse() {}

    public DebitResponse(String correlationId, Long accountId, Double balance, Long version, boolean success, String reason) {
        this.correlationId = correlationId; this.accountId = accountId;
        this.balance = balance; this.version = version;
        this.success = success; this.reason = reason;
    }

    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String v) { this.correlationId = v; }
    public Long getAccountId() { return accountId; }
    public void setAccountId(Long v) { this.accountId = v; }
    public Double getBalance() { return balance; }
    public void setBalance(Double v) { this.balance = v; }
    public Long getVersion() { return version; }
    public void setVersion(Long v) { this.version = v; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean v) { this.success = v; }
    public String getReason() { return reason; }
    public void setReason(String v) { this.reason = v; }
}
