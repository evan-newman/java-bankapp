package com.tcs.finalproject.bankapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDetailsDTO {

    private String name;

    private double originalAmount;

    private int monthlyTerm;

    private double interestRate;

}
