package com.tcs.finalproject.bankapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcs.finalproject.bankapp.entity.AccountEntries;

public interface AccountEntriesRepository extends JpaRepository<AccountEntries, Long> {
    
    public AccountEntries findByAccountId(Long accId);
}
