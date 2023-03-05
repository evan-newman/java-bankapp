package com.tcs.finalproject.bankapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tcs.finalproject.bankapp.entity.AccountEntries;
import com.tcs.finalproject.bankapp.entity.Accounts;
import com.tcs.finalproject.bankapp.entity.CreditDetails;
import com.tcs.finalproject.bankapp.repository.AccountEntriesRepository;
import com.tcs.finalproject.bankapp.repository.AccountsRepository;
import com.tcs.finalproject.bankapp.repository.CreditDetailsRepository;

@Service
public class CreditDetailsService {
    private final CreditDetailsRepository creditDetailsRepo;
    private final AccountsRepository accountsRepo;
    private final AccountEntriesRepository accountEntriesRepo;

    public CreditDetailsService(CreditDetailsRepository creditDetailsRepo, 
                                AccountsRepository accountsRepo,
                                AccountEntriesRepository accountEntriesRepo) {
        this.creditDetailsRepo = creditDetailsRepo;
        this.accountsRepo = accountsRepo;
        this.accountEntriesRepo = accountEntriesRepo;
    }

    public List<CreditDetails> getAllCreditDetails() {
        return creditDetailsRepo.findAll();
    }

    public CreditDetails getCreditDetailsById(Long id) {
        return creditDetailsRepo.findById(id).get();
    }

    public CreditDetails saveOrUpdateCreditDetails(CreditDetails creditDet) {
        return creditDetailsRepo.save(creditDet);
    }

    public void deleteCreditDetails(Long id) {
        creditDetailsRepo.deleteById(id);
    } 

    // ------------------------------------------------------------
    public CreditDetails updateCreditDetails(CreditDetails creditDet) {
        Long accId = creditDet.getAccountId();
        Accounts acc = accountsRepo.findById(accId).get(); // doesn't actually error handle???
        if (acc == null) {
            //error here
            return null;
        } else if (acc.getAccountTypeId() != 4) {
            //error here
            return null;
        }

        AccountEntries accEnt = accountEntriesRepo.findById(accId).get();
        if (accEnt.getAmount() < creditDet.getCreditLimit()) {
            //error here
            return null;
        }

        return creditDetailsRepo.save(creditDet);
    }

}
