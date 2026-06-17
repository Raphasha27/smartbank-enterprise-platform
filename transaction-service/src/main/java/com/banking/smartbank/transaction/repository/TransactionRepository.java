package com.banking.smartbank.transaction.repository;

import com.banking.smartbank.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromAccountIdOrToAccountIdOrderByTimestampDesc(Long fromId, Long toId);
    Optional<Transaction> findByIdempotencyKey(String idempotencyKey);
}
