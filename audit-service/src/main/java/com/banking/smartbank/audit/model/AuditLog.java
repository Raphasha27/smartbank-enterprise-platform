package com.banking.smartbank.audit.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name = "audit_logs")
public class AuditLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String action;
    private String userEmail;
    private String details;
    private String serviceName;
    private LocalDateTime timestamp;

    public AuditLog() { this.timestamp = LocalDateTime.now(); }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAction() { return action; }
    public void setAction(String a) { this.action = a; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String u) { this.userEmail = u; }
    public String getDetails() { return details; }
    public void setDetails(String d) { this.details = d; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String s) { this.serviceName = s; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
