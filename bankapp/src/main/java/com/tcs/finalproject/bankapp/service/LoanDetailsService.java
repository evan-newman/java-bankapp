package com.tcs.finalproject.bankapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tcs.finalproject.bankapp.entity.AccountEntries;
import com.tcs.finalproject.bankapp.entity.Accounts;
import com.tcs.finalproject.bankapp.entity.LoanDetails;
import com.tcs.finalproject.bankapp.exception.BankException;
import com.tcs.finalproject.bankapp.repository.AccountEntriesRepository;
import com.tcs.finalproject.bankapp.repository.AccountsRepository;
import com.tcs.finalproject.bankapp.repository.LoanDetailsRepository;

@Service
public class LoanDetailsService {
    private final LoanDetailsRepository loanDetailsRepo;
    private final AccountsRepository accountsRepo;
    private final AccountEntriesRepository accountEntriesRepo;

    public LoanDetailsService(LoanDetailsRepository loanDetailsRepo, 
                              AccountsRepository accountsRepo,
                              AccountEntriesRepository accountEntriesRepo) {
        this.loanDetailsRepo = loanDetailsRepo;
        this.accountsRepo = accountsRepo;
        this.accountEntriesRepo = accountEntriesRepo;
    }

    public List<LoanDetails> getAllLoanDetails() {
        return loanDetailsRepo.findAll();
    }

    public LoanDetails getLoanDetailsById(Long id) {
        return loanDetailsRepo.findById(id).get();
    }

    public LoanDetails saveOrUpdateLoanDetails(LoanDetails loanDet) {
        return loanDetailsRepo.save(loanDet);
    }

    public void deleteLoanDetails(Long id) {
        loanDetailsRepo.deleteById(id);
    }

    // ------------------------------------------------------------
    public LoanDetails updateLoanDetails(LoanDetails loanDet) throws BankException {
        Long accId = loanDet.getAccountId();
        Optional<Accounts> accOpt = accountsRepo.findById(accId);
        if (!accOpt.isPresent()) {
            throw new BankException("Account not found");
        }
        
        Accounts acc = accOpt.get();
        if (acc.getAccountTypeId() != 3) {
            throw new BankException("Incorrect account type not a loan");
        }

        LoanDetails ogLoanDet = loanDetailsRepo.findByAccountId(accId);
        if (ogLoanDet.getOriginalAmount() != loanDet.getOriginalAmount())  {
            throw new BankException("Not permitted to adjust the original loan amount");
        }
        loanDet.setId(ogLoanDet.getId());
        return loanDetailsRepo.save(loanDet);
    }
}
