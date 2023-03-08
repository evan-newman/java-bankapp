package com.tcs.finalproject.bankapp.entity;

import java.sql.Timestamp;

import com.tcs.finalproject.bankapp.model.LoanDetailsDTO;

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
@Table(name = "LOAN_DETAILS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "creation_date")
    private Timestamp creationDate;

    @Column(name = "original_amount")
    private double originalAmount;

    @Column(name = "monthly_payment")
    private double monthlyPayment;

    @Column(name = "interest_rate")
    private double interestRate;

    @Column(name = "monthly_term")
    private int monthlyTerm;

    public LoanDetails(LoanDetailsDTO loanDTO, Long accId) {
        this.accountId = accId;
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.originalAmount = loanDTO.getOriginalAmount();
        this.monthlyTerm = loanDTO.getMonthlyTerm();
        this.interestRate = loanDTO.getInterestRate();

        this.monthlyPayment = ((-this.originalAmount * this.interestRate * (this.monthlyTerm / 12)) + 
                               -this.originalAmount) / this.monthlyTerm;
    }
}
