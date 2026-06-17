package com.banking.smartbank.ledger.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ledger_entries")
public class LedgerEntry {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transactionRef;
    private Long accountId;
    private String entryType;
    private Double amount;
    private String currency;
    private String description;
    private LocalDateTime timestamp;

    public LedgerEntry() { this.timestamp = LocalDateTime.now(); }

    public LedgerEntry(String transactionRef, Long accountId, String entryType, Double amount, String currency, String description) {
        this.transactionRef = transactionRef; this.accountId = accountId;
        this.entryType = entryType; this.amount = amount;
        this.currency = currency; this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getTransactionRef() { return transactionRef; }
    public void setTransactionRef(String v) { this.transactionRef = v; }
    public Long getAccountId() { return accountId; }
    public void setAccountId(Long v) { this.accountId = v; }
    public String getEntryType() { return entryType; }
    public void setEntryType(String v) { this.entryType = v; }
    public Double getAmount() { return amount; }
    public void setAmount(Double v) { this.amount = v; }
    public String getCurrency() { return currency; }
    public void setCurrency(String v) { this.currency = v; }
    public String getDescription() { return description; }
    public void setDescription(String v) { this.description = v; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
