package com.banking.smartbank.notification.controller;

import com.banking.smartbank.notification.model.Notification;
import com.banking.smartbank.notification.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService service;

    public NotificationController(NotificationService s) { this.service = s; }

    @PostMapping
    public ResponseEntity<Notification> send(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(service.send(
            req.get("userEmail"), req.get("type"), req.get("message")));
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<List<Notification>> getByUser(@PathVariable String email) {
        return ResponseEntity.ok(service.getUserNotifications(email));
    }

    @GetMapping("/user/{email}/unread")
    public ResponseEntity<List<Notification>> getUnread(@PathVariable String email) {
        return ResponseEntity.ok(service.getUnread(email));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Map<String, String>> markRead(@PathVariable Long id) {
        service.markRead(id);
        return ResponseEntity.ok(Map.of("status", "READ"));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "notification-service"));
    }
}
