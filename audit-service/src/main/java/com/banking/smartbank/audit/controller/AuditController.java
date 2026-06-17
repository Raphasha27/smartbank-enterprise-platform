package com.banking.smartbank.audit.controller;

import com.banking.smartbank.audit.model.AuditLog;
import com.banking.smartbank.audit.service.AuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/audit")
public class AuditController {
    private final AuditService service;

    public AuditController(AuditService s) { this.service = s; }

    @PostMapping("/logs")
    public ResponseEntity<AuditLog> log(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(service.log(
            req.get("action"), req.get("userEmail"),
            req.get("details"), req.get("serviceName")));
    }

    @GetMapping("/logs")
    public ResponseEntity<List<AuditLog>> getAll() {
        return ResponseEntity.ok(service.getAllLogs());
    }

    @GetMapping("/logs/user/{email}")
    public ResponseEntity<List<AuditLog>> getByUser(@PathVariable String email) {
        return ResponseEntity.ok(service.getUserLogs(email));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "audit-service"));
    }
}
