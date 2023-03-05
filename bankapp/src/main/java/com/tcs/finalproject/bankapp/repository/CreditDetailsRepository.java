package com.tcs.finalproject.bankapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcs.finalproject.bankapp.entity.CreditDetails;

public interface CreditDetailsRepository extends JpaRepository<CreditDetails, Long> {
    
    CreditDetails findByAccountId(Long accId);
}
