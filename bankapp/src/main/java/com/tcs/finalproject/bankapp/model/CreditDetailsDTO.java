package com.tcs.finalproject.bankapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditDetailsDTO {

    private String name;

    private double creditLimit;
    private String ccNumber;
    private int monthExpire;
    private int yearExpire;
    private int ccv;
}
