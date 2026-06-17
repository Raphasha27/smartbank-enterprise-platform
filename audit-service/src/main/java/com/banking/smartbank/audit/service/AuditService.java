package com.banking.smartbank.audit.service;

import com.banking.smartbank.audit.model.AuditLog;
import com.banking.smartbank.audit.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuditService {
    private final AuditLogRepository repo;

    public AuditService(AuditLogRepository r) { this.repo = r; }

    public AuditLog log(String action, String userEmail, String details, String serviceName) {
        AuditLog log = new AuditLog();
        log.setAction(action); log.setUserEmail(userEmail);
        log.setDetails(details); log.setServiceName(serviceName);
        return repo.save(log);
    }

    public List<AuditLog> getUserLogs(String userEmail) {
        return repo.findByUserEmailOrderByTimestampDesc(userEmail);
    }

    public List<AuditLog> getAllLogs() {
        return repo.findAll();
    }
}
