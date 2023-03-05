package com.tcs.finalproject.bankapp.entity;

import java.sql.Timestamp;

import com.tcs.finalproject.bankapp.model.CreditDetailsDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CREDIT_DETAILS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "credit_limit")
    private double creditLimit;

    @Column(name = "creation_date")
    private Timestamp creationDate;

    @Column(name = "cc_number")
    private String ccNumber;

    @Column(name = "month_expire")
    private int monthExpire;

    @Column(name = "year_expire")
    private int yearExpire;

    @Column
    private int ccv; 

    public CreditDetails(CreditDetailsDTO credDTO, Long accId) {
        this.accountId = accId;
        this.creditLimit = credDTO.getCreditLimit();
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.ccNumber = credDTO.getCcNumber();
        this.monthExpire = credDTO.getMonthExpire();
        this.yearExpire = credDTO.getYearExpire();
        this.ccv = credDTO.getCcv();
    }
}
