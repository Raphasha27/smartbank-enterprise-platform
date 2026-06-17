package com.banking.smartbank.loan.controller;

import com.banking.smartbank.loan.model.Loan;
import com.banking.smartbank.loan.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/loans")
public class LoanController {
    private final LoanService service;

    public LoanController(LoanService s) { this.service = s; }

    @PostMapping
    public ResponseEntity<Loan> apply(@RequestBody Map<String, Object> req) {
        return ResponseEntity.ok(service.apply(
            Long.valueOf(req.get("userId").toString()),
            Double.valueOf(req.get("amount").toString()),
            Double.valueOf(req.get("interestRate").toString()),
            Integer.valueOf(req.get("termMonths").toString())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getLoan(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Loan>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getUserLoans(userId));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Loan> approve(@PathVariable Long id) {
        return ResponseEntity.ok(service.approve(id));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "loan-service"));
    }
}
