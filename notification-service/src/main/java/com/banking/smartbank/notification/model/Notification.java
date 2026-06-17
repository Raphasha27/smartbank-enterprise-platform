package com.banking.smartbank.notification.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name = "notifications")
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userEmail;
    private String type;
    private String message;
    private boolean read = false;
    private LocalDateTime createdAt;

    public Notification() { this.createdAt = LocalDateTime.now(); }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String u) { this.userEmail = u; }
    public String getType() { return type; }
    public void setType(String t) { this.type = t; }
    public String getMessage() { return message; }
    public void setMessage(String m) { this.message = m; }
    public boolean isRead() { return read; }
    public void setRead(boolean r) { this.read = r; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
