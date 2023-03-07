package com.tcs.finalproject.bankapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tcs.finalproject.bankapp.entity.AccountEntries;
import com.tcs.finalproject.bankapp.entity.Accounts;
import com.tcs.finalproject.bankapp.entity.CreditDetails;
import com.tcs.finalproject.bankapp.repository.AccountEntriesRepository;
import com.tcs.finalproject.bankapp.repository.AccountsRepository;
import com.tcs.finalproject.bankapp.repository.CreditDetailsRepository;
import com.tcs.finalproject.bankapp.exception.BankException;

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
    public CreditDetails updateCreditDetails(CreditDetails creditDet) throws BankException {
        Long accId = creditDet.getAccountId();
        Optional<Accounts> accOpt = accountsRepo.findById(accId);
        if (!accOpt.isPresent()) {
            throw new BankException("Account not found");
        }
        Accounts acc = accOpt.get(); 
        if (acc.getAccountTypeId() != 4) {
            throw new BankException("Incorrect account type not a credit card");
        }

        AccountEntries accEnt = accountEntriesRepo.findById(accId).get();
        if (accEnt.getAmount() < creditDet.getCreditLimit()) {
            throw new BankException("Invalid amount sets amount past credit limit");
        }

        return creditDetailsRepo.save(creditDet);
    }

}
