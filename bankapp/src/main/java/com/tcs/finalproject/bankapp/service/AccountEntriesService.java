package com.tcs.finalproject.bankapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tcs.finalproject.bankapp.entity.AccountEntries;
import com.tcs.finalproject.bankapp.repository.AccountEntriesRepository;

@Service
public class AccountEntriesService {
    private final AccountEntriesRepository accountEntriesRepo;

    public AccountEntriesService(AccountEntriesRepository accountEntriesRepo) {
        this.accountEntriesRepo = accountEntriesRepo;
    }

    public List<AccountEntries> getAllAccountEntries() {
        return accountEntriesRepo.findAll();
    }

    public AccountEntries getAccountEntryById(Long id) {
        return accountEntriesRepo.findById(id).get();
    }

    public AccountEntries saveOrUpdateAccountEntry(AccountEntries accountEntry) {
        return accountEntriesRepo.save(accountEntry);
    }

    public void deleteAccountEntry(Long id) {
        accountEntriesRepo.deleteById(id);
    } 
}
