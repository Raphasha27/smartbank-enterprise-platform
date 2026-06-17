package com.banking.smartbank.account.service;

import com.banking.smartbank.account.model.Account;
import com.banking.smartbank.account.repository.AccountRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AccountService {
    private final AccountRepository repo;

    public AccountService(AccountRepository r) { this.repo = r; }

    public Account createAccount(Long userId, String type) {
        Account acc = new Account();
        acc.setUserId(userId); acc.setType(type);
        return repo.save(acc);
    }

    public Account getAccount(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public List<Account> getUserAccounts(Long userId) {
        return repo.findByUserId(userId);
    }

    public void updateBalance(Long id, Double delta) {
        Account acc = getAccount(id);
        acc.setBalance(acc.getBalance() + delta);
        repo.save(acc);
    }
}
