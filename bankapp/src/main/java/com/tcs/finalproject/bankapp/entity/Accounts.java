package com.tcs.finalproject.bankapp.entity;

import com.tcs.finalproject.bankapp.model.CreditDetailsDTO;
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
@Table(name = "ACCOUNTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Accounts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column
    private String name;

    @Column(name = "account_type_id")
    private Long accountTypeId;

    @Column
    private boolean open;

    public Accounts(CreditDetailsDTO credDTO, Long userId) {
        this.userId = userId;
        this.name = credDTO.getName();
        this.accountTypeId = 4L;
        this.open = true;
    }

    public Accounts(LoanDetailsDTO loanDTO, Long userId) {
        this.userId = userId;
        this.name = loanDTO.getName();
        this.accountTypeId = 3L;
        this.open = true;
    }
}
