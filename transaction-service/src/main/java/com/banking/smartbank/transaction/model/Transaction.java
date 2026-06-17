package com.banking.smartbank.transaction.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name = "transactions")
public class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long fromAccountId;
    private Long toAccountId;
    private Double amount;
    private String type;
    private String status;
    private LocalDateTime timestamp;

    public Transaction() { this.timestamp = LocalDateTime.now(); }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getFromAccountId() { return fromAccountId; }
    public void setFromAccountId(Long f) { this.fromAccountId = f; }
    public Long getToAccountId() { return toAccountId; }
    public void setToAccountId(Long t) { this.toAccountId = t; }
    public Double getAmount() { return amount; }
    public void setAmount(Double a) { this.amount = a; }
    public String getType() { return type; }
    public void setType(String t) { this.type = t; }
    public String getStatus() { return status; }
    public void setStatus(String s) { this.status = s; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
