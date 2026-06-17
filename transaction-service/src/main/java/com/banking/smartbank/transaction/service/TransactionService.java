package com.banking.smartbank.transaction.service;

import com.banking.smartbank.transaction.model.Transaction;
import com.banking.smartbank.transaction.repository.TransactionRepository;
import com.banking.smartbank.transaction.event.TransactionEventPublisher;
import com.banking.smartbank.transaction.event.DebitRequest;
import com.banking.smartbank.transaction.event.DebitResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionService {
    private final TransactionRepository txnRepo;
    private final TransactionEventPublisher publisher;
    private final Map<String, CompletableFuture<DebitResponse>> pendingRequests = new ConcurrentHashMap<>();

    public TransactionService(TransactionRepository tr, TransactionEventPublisher p) {
        this.txnRepo = tr;
        this.publisher = p;
    }

    public void registerPendingResponse(String correlationId, CompletableFuture<DebitResponse> future) {
        pendingRequests.put(correlationId, future);
    }

    public void completePendingResponse(DebitResponse response) {
        CompletableFuture<DebitResponse> future = pendingRequests.remove(response.getCorrelationId());
        if (future != null) future.complete(response);
    }

    @Transactional
    public Transaction transfer(Long fromAccountId, Long toAccountId, Double amount, String idempotencyKey) {
        if (idempotencyKey != null && txnRepo.findByIdempotencyKey(idempotencyKey).isPresent()) {
            return txnRepo.findByIdempotencyKey(idempotencyKey).get();
        }

        Transaction txn = new Transaction();
        txn.setFromAccountId(fromAccountId); txn.setToAccountId(toAccountId);
        txn.setAmount(amount); txn.setType("TRANSFER"); txn.setStatus("PENDING");
        txn.setIdempotencyKey(idempotencyKey);
        txn = txnRepo.save(txn);

        String debitCorrelationId = UUID.randomUUID().toString();
        String creditCorrelationId = UUID.randomUUID().toString();

        CompletableFuture<DebitResponse> debitFuture = new CompletableFuture<>();
        registerPendingResponse(debitCorrelationId, debitFuture);

        publisher.publishDebitRequest(new DebitRequest(debitCorrelationId, fromAccountId, amount, "DEBIT", 0L));

        DebitResponse debitResult;
        try {
            debitResult = debitFuture.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            txn.setStatus("PENDING_REVERSAL");
            txnRepo.save(txn);
            publisher.publishPendingReversal(txn.getId(), fromAccountId, toAccountId, amount);
            return txn;
        }

        if (!debitResult.isSuccess()) {
            txn.setStatus("FAILED");
            txn.setDescription(debitResult.getReason());
            return txnRepo.save(txn);
        }

        CompletableFuture<DebitResponse> creditFuture = new CompletableFuture<>();
        registerPendingResponse(creditCorrelationId, creditFuture);
        publisher.publishDebitRequest(new DebitRequest(creditCorrelationId, toAccountId, amount, "CREDIT", 0L));

        try {
            DebitResponse creditResult = creditFuture.get(10, TimeUnit.SECONDS);
            if (!creditResult.isSuccess()) {
                txn.setStatus("PENDING_REVERSAL");
                txnRepo.save(txn);
                publisher.publishPendingReversal(txn.getId(), fromAccountId, toAccountId, amount);
                return txn;
            }
        } catch (Exception e) {
            txn.setStatus("PENDING_REVERSAL");
            txnRepo.save(txn);
            publisher.publishPendingReversal(txn.getId(), fromAccountId, toAccountId, amount);
            return txn;
        }

        txn.setStatus("COMPLETED");
        txn = txnRepo.save(txn);
        publisher.publish(txn, "system");
        return txn;
    }

    public java.util.List<Transaction> getAccountTransactions(Long accountId) {
        return txnRepo.findByFromAccountIdOrToAccountIdOrderByTimestampDesc(accountId, accountId);
    }
}
