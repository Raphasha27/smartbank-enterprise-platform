package com.banking.smartbank.transaction.service;

import com.banking.smartbank.transaction.model.Transaction;
import com.banking.smartbank.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {
    private final TransactionRepository txnRepo;
    private final RestTemplate rest;

    public TransactionService(TransactionRepository tr) {
        this.txnRepo = tr;
        this.rest = new RestTemplate();
    }

    @Transactional
    public Transaction transfer(Long fromAccountId, Long toAccountId, Double amount) {
        Double fromBalance = rest.getForObject(
            "http://localhost:8082/accounts/" + fromAccountId, Map.class)
            .get("balance") instanceof Double d ? d : 0.0;

        if (fromBalance < amount) {
            throw new RuntimeException("Insufficient funds");
        }

        rest.put("http://localhost:8082/accounts/" + fromAccountId + "/balance?delta=" + (-amount), null);
        rest.put("http://localhost:8082/accounts/" + toAccountId + "/balance?delta=" + amount, null);

        Transaction txn = new Transaction();
        txn.setFromAccountId(fromAccountId); txn.setToAccountId(toAccountId);
        txn.setAmount(amount); txn.setType("TRANSFER"); txn.setStatus("COMPLETED");
        return txnRepo.save(txn);
    }

    public List<Transaction> getAccountTransactions(Long accountId) {
        return txnRepo.findByFromAccountIdOrToAccountIdOrderByTimestampDesc(accountId, accountId);
    }
}
