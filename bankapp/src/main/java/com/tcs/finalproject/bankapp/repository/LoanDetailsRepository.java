package com.tcs.finalproject.bankapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcs.finalproject.bankapp.entity.LoanDetails;

public interface LoanDetailsRepository extends JpaRepository<LoanDetails, Long> {
    
    LoanDetails findByAccountId(Long accId);
}
