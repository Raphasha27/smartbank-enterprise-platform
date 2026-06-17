package com.banking.smartbank.account.repository;

import com.banking.smartbank.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserId(Long userId);

    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance - ?2, a.version = a.version + 1 WHERE a.id = ?1 AND a.version = ?3 AND a.balance >= ?2 AND a.status = 'ACTIVE'")
    int atomicDebit(Long accountId, Double amount, Long expectedVersion);

    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance + ?2, a.version = a.version + 1 WHERE a.id = ?1 AND a.status = 'ACTIVE'")
    int atomicCredit(Long accountId, Double amount);
}
