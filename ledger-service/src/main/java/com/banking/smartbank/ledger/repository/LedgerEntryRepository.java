package com.banking.smartbank.ledger.repository;

import com.banking.smartbank.ledger.model.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {
    List<LedgerEntry> findByAccountIdOrderByTimestampDesc(Long accountId);
    List<LedgerEntry> findByTransactionRef(String transactionRef);

    @Query("SELECT COALESCE(SUM(CASE WHEN e.entryType = 'CREDIT' THEN e.amount ELSE -e.amount END), 0.0) FROM LedgerEntry e WHERE e.accountId = ?1")
    Double calculateBalance(Long accountId);
}
