package com.banking.smartbank.ledger.controller;

import com.banking.smartbank.ledger.model.LedgerEntry;
import com.banking.smartbank.ledger.service.LedgerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ledger")
public class LedgerController {
    private final LedgerService service;

    public LedgerController(LedgerService s) { this.service = s; }

    @PostMapping("/entries")
    public ResponseEntity<Map<String, String>> recordTransfer(@RequestBody Map<String, Object> req) {
        service.recordTransfer(
            req.get("transactionRef").toString(),
            Long.valueOf(req.get("fromAccountId").toString()),
            Long.valueOf(req.get("toAccountId").toString()),
            Double.valueOf(req.get("amount").toString()),
            req.getOrDefault("currency", "ZAR").toString());
        return ResponseEntity.ok(Map.of("status", "RECORDED"));
    }

    @GetMapping("/accounts/{id}/balance")
    public ResponseEntity<Map<String, Object>> getBalance(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("accountId", id, "balance", service.getBalance(id)));
    }

    @GetMapping("/accounts/{id}/entries")
    public ResponseEntity<List<LedgerEntry>> getEntries(@PathVariable Long id) {
        return ResponseEntity.ok(service.getEntries(id));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "ledger-service"));
    }
}
