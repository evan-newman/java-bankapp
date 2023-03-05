package com.tcs.finalproject.bankapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcs.finalproject.bankapp.entity.AccountTypes;

public interface AccountTypesRepository extends JpaRepository<AccountTypes, Long> {
    
}
