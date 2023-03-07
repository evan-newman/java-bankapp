package com.tcs.finalproject.bankapp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.tcs.finalproject.bankapp.entity.Accounts;
import com.tcs.finalproject.bankapp.entity.CreditDetails;
import com.tcs.finalproject.bankapp.entity.LoanDetails;
import com.tcs.finalproject.bankapp.model.CreditDetailsDTO;
import com.tcs.finalproject.bankapp.model.LoanDetailsDTO;
import com.tcs.finalproject.bankapp.service.AccountsService;
import com.tcs.finalproject.bankapp.service.CreditDetailsService;
import com.tcs.finalproject.bankapp.service.LoanDetailsService;
import com.tcs.finalproject.bankapp.exception.BankException;

@RestController
public class AdminController {
    private final AccountsService accountsService;
    private final CreditDetailsService creditDetailsService;
    private final LoanDetailsService loanDetailsService;

    public AdminController(AccountsService accountsService, 
                           CreditDetailsService creditDetailsService,
                           LoanDetailsService loanDetailsService) {
        this.accountsService = accountsService;
        this.creditDetailsService = creditDetailsService;
        this.loanDetailsService = loanDetailsService;
    }

    @GetMapping("/admin/accounts")
    public ResponseEntity<List<Accounts>> getAllAccounts() {
        return ResponseEntity.status(HttpStatus.OK).body(accountsService.getAllAccounts());
    }

    @GetMapping("/admin/accounts/{id}/close")
    public ResponseEntity<Accounts> closeAnAccount(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(accountsService.closeAccountById(id));
        } catch (BankException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/admin/accounts/update")
    public ResponseEntity<Accounts> updateAnAccount(@RequestBody Accounts acc) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(accountsService.updateAccount(acc));
        } catch (BankException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/admin/accounts/update/credit")
    public ResponseEntity<CreditDetails> updateCreditAccountDetails(@RequestBody CreditDetails credDet) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(creditDetailsService.updateCreditDetails(credDet));
        } catch (BankException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/admin/accounts/update/loan")
    public ResponseEntity<LoanDetails> updateLoanAccountDetails(@RequestBody LoanDetails loanDet) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(loanDetailsService.updateLoanDetails(loanDet));
        } catch (BankException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/admin/accounts/new/credit/{userId}")
    public ResponseEntity<CreditDetails> createCreditAccountForUser(@PathVariable Long userId, @RequestBody CreditDetailsDTO credDTO) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(accountsService.createCreditAccount(userId, credDTO));
        } catch (BankException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/admin/accounts/new/loan/{userId}")
    public ResponseEntity<LoanDetails> createLoanAccountForUser(@PathVariable Long userId, @RequestBody LoanDetailsDTO loanDTO) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(accountsService.createLoanAccount(userId, loanDTO));
        } catch (BankException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
