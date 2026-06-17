package com.banking.smartbank.notification.service;

import com.banking.smartbank.notification.model.Notification;
import com.banking.smartbank.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository repo;

    public NotificationService(NotificationRepository r) { this.repo = r; }

    public Notification send(String userEmail, String type, String message) {
        Notification n = new Notification();
        n.setUserEmail(userEmail); n.setType(type); n.setMessage(message);
        return repo.save(n);
    }

    public List<Notification> getUserNotifications(String userEmail) {
        return repo.findByUserEmailOrderByCreatedAtDesc(userEmail);
    }

    public List<Notification> getUnread(String userEmail) {
        return repo.findByUserEmailAndReadFalse(userEmail);
    }

    public void markRead(Long id) {
        Notification n = repo.findById(id).orElseThrow();
        n.setRead(true);
        repo.save(n);
    }
}
