package com.tcs.finalproject.bankapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tcs.finalproject.bankapp.entity.AccountEntries;
import com.tcs.finalproject.bankapp.entity.Accounts;
import com.tcs.finalproject.bankapp.entity.LoanDetails;
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
    public LoanDetails updateLoanDetails(LoanDetails loanDet) {
        Long accId = loanDet.getAccountId();
        Accounts acc = accountsRepo.findById(accId).get();
        if (acc == null) {
            //error here
            return null;
        } else if (acc.getAccountTypeId() != 3) {
            //error here
            return null;
        }

        LoanDetails ogLoanDet = loanDetailsRepo.findByAccountId(accId);
        AccountEntries accEnt = accountEntriesRepo.findByAccountId(accId);
        if (ogLoanDet.getOriginalAmount() > loanDet.getOriginalAmount())  {
            //error here
            return null;
        } else if (accEnt.getAmount() < loanDet.getOriginalAmount()) {
            //error here
            return null;
        }
        loanDet.setId(ogLoanDet.getId());
        return loanDetailsRepo.save(loanDet);
    }
}
