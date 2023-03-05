package com.tcs.finalproject.bankapp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.finalproject.bankapp.entity.Accounts;
import com.tcs.finalproject.bankapp.entity.CreditDetails;
import com.tcs.finalproject.bankapp.entity.LoanDetails;
import com.tcs.finalproject.bankapp.model.CreditDetailsDTO;
import com.tcs.finalproject.bankapp.model.LoanDetailsDTO;
import com.tcs.finalproject.bankapp.service.AccountsService;
import com.tcs.finalproject.bankapp.service.CreditDetailsService;
import com.tcs.finalproject.bankapp.service.LoanDetailsService;

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
    public List<Accounts> getAllAccounts() {
        return accountsService.getAllAccounts();
    }

    @GetMapping("/admin/accounts/{id}/close")
    public Accounts closeAnAccount(@PathVariable Long id) {
        return accountsService.closeAccountById(id);
    }

    @PostMapping("/admin/accounts/update")
    public Accounts updateAnAccount(@RequestBody Accounts acc) {
        return accountsService.updateAccount(acc);
    }

    @PostMapping("/admin/accounts/update/credit")
    public CreditDetails updateCreditAccountDetails(@RequestBody CreditDetails credDet) {
        return creditDetailsService.updateCreditDetails(credDet);
    }

    @PostMapping("/admin/accounts/update/loan")
    public LoanDetails updateCreditAccountDetails(@RequestBody LoanDetails loanDet) {
        return loanDetailsService.updateLoanDetails(loanDet);
    }

    @PostMapping("/admin/accounts/new/credit/{userId}")
    public CreditDetails createCreditAccountForUser(@PathVariable Long userId, @RequestBody CreditDetailsDTO credDTO) {
        return accountsService.createCreditAccount(userId, credDTO);
    }

    @PostMapping("/admin/accounts/new/loan/{userId}")
    public LoanDetails createLoanAccountForUser(@PathVariable Long userId, @RequestBody LoanDetailsDTO loanDTO) {
        return accountsService.createLoanAccount(userId, loanDTO);
    }
}
