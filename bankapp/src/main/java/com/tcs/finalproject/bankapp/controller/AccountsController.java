package com.tcs.finalproject.bankapp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.finalproject.bankapp.entity.Accounts;
import com.tcs.finalproject.bankapp.service.AccountsService;

@RestController
public class AccountsController {
    private final AccountsService accountsService;

    public AccountsController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @GetMapping("/account")
    public List<Accounts> getAllAccounts() {
        return accountsService.getAllAccounts();
    }

    @GetMapping("/account/{id}")
    public Accounts getAccountById(@PathVariable Long id) {
        return accountsService.getAccountById(id);
    }

    @PostMapping("/account")
    public Accounts saveOrUpdateAccount(@RequestBody Accounts acc) {
        return accountsService.saveOrUpdateAccount(acc);
    }

    @DeleteMapping("/account/{id}")
    public void deleteAccount(@PathVariable Long id) {
        accountsService.deleteAccount(id);
    }
}
