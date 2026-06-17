package com.banking.smartbank.account.controller;

import com.banking.smartbank.account.model.Account;
import com.banking.smartbank.account.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService service;

    public AccountController(AccountService s) { this.service = s; }

    @PostMapping
    public ResponseEntity<Account> create(@RequestBody Map<String, Object> req) {
        return ResponseEntity.ok(service.createAccount(
            Long.valueOf(req.get("userId").toString()), (String)req.get("type")));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAccount(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Account>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getUserAccounts(userId));
    }

    @PutMapping("/{id}/balance")
    public ResponseEntity<Map<String, String>> updateBalance(@PathVariable Long id, @RequestParam Double delta) {
        service.updateBalance(id, delta);
        return ResponseEntity.ok(Map.of("status", "UPDATED"));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "account-service"));
    }
}
