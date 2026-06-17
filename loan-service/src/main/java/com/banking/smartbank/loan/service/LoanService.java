package com.banking.smartbank.loan.service;

import com.banking.smartbank.loan.model.Loan;
import com.banking.smartbank.loan.repository.LoanRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LoanService {
    private final LoanRepository repo;

    public LoanService(LoanRepository r) { this.repo = r; }

    public Loan apply(Long userId, Double amount, Double interestRate, Integer termMonths) {
        Loan loan = new Loan();
        loan.setUserId(userId); loan.setAmount(amount);
        loan.setInterestRate(interestRate); loan.setTermMonths(termMonths);
        return repo.save(loan);
    }

    public Loan approve(Long loanId) {
        Loan loan = repo.findById(loanId).orElseThrow();
        loan.setStatus("APPROVED");
        return repo.save(loan);
    }

    public List<Loan> getUserLoans(Long userId) {
        return repo.findByUserId(userId);
    }

    public Loan getLoan(Long id) {
        return repo.findById(id).orElseThrow();
    }
}
