package com.banking.smartbank.loan.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name = "loans")
public class Loan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Double amount;
    private Double interestRate;
    private Integer termMonths;
    private String status = "PENDING";
    private LocalDateTime createdAt;

    public Loan() { this.createdAt = LocalDateTime.now(); }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long u) { this.userId = u; }
    public Double getAmount() { return amount; }
    public void setAmount(Double a) { this.amount = a; }
    public Double getInterestRate() { return interestRate; }
    public void setInterestRate(Double r) { this.interestRate = r; }
    public Integer getTermMonths() { return termMonths; }
    public void setTermMonths(Integer t) { this.termMonths = t; }
    public String getStatus() { return status; }
    public void setStatus(String s) { this.status = s; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
