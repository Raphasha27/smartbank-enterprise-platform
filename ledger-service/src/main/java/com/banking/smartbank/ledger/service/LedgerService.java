package com.banking.smartbank.ledger.service;

import com.banking.smartbank.ledger.model.LedgerEntry;
import com.banking.smartbank.ledger.repository.LedgerEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class LedgerService {
    private final LedgerEntryRepository repo;

    public LedgerService(LedgerEntryRepository r) { this.repo = r; }

    @Transactional
    public void recordTransfer(String transactionRef, Long fromAccount, Long toAccount, Double amount, String currency) {
        repo.save(new LedgerEntry(transactionRef, fromAccount, "DEBIT", amount, currency, "Transfer to " + toAccount));
        repo.save(new LedgerEntry(transactionRef, toAccount, "CREDIT", amount, currency, "Transfer from " + fromAccount));
    }

    public Double getBalance(Long accountId) {
        Double balance = repo.calculateBalance(accountId);
        return balance != null ? balance : 0.0;
    }

    public List<LedgerEntry> getEntries(Long accountId) {
        return repo.findByAccountIdOrderByTimestampDesc(accountId);
    }
}
