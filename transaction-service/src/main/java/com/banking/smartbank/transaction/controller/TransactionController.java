package com.banking.smartbank.transaction.controller;

import com.banking.smartbank.transaction.model.Transaction;
import com.banking.smartbank.transaction.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService service;

    public TransactionController(TransactionService s) { this.service = s; }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(
            @RequestBody Map<String, Object> req,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
        return ResponseEntity.ok(service.transfer(
            Long.valueOf(req.get("fromAccountId").toString()),
            Long.valueOf(req.get("toAccountId").toString()),
            Double.valueOf(req.get("amount").toString()),
            idempotencyKey));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getByAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(service.getAccountTransactions(accountId));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "transaction-service"));
    }
}
