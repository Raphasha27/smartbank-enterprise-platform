package com.banking.smartbank.account.model;

import jakarta.persistence.*;

@Entity @Table(name = "accounts")
public class Account {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String type;
    private Double balance = 0.0;
    @Version
    private Long version = 0L;
    private String status = "ACTIVE";

    public Account() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long u) { this.userId = u; }
    public String getType() { return type; }
    public void setType(String t) { this.type = t; }
    public Double getBalance() { return balance; }
    public void setBalance(Double b) { this.balance = b; }
    public Long getVersion() { return version; }
    public void setVersion(Long v) { this.version = v; }
    public String getStatus() { return status; }
    public void setStatus(String s) { this.status = s; }
}
