package com.tcs.finalproject.bankapp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.finalproject.bankapp.entity.AccountEntries;
import com.tcs.finalproject.bankapp.service.AccountEntriesService;

@RestController
public class AccountEntriesController {
    private final AccountEntriesService accountEntriesService;

    public AccountEntriesController(AccountEntriesService accountEntriesService) {
        this.accountEntriesService = accountEntriesService;
    }

    @GetMapping("/accountentry")
    public List<AccountEntries> getAllAccountEntries() {
        return accountEntriesService.getAllAccountEntries();
    }

    @GetMapping("/accountentry/{id}")
    public AccountEntries getAccountEntryById(@PathVariable Long id) {
        return accountEntriesService.getAccountEntryById(id);
    }

    @PostMapping("/accountentry")
    public AccountEntries saveOrUpdateAccountEntry(@RequestBody AccountEntries accEnt) {
        return accountEntriesService.saveOrUpdateAccountEntry(accEnt);
    }

    @DeleteMapping("/accountentry/{id}")
    public void deleteAccount(@PathVariable Long id) {
        accountEntriesService.deleteAccountEntry(id);
    }
}
