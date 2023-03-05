package com.tcs.finalproject.bankapp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.finalproject.bankapp.entity.LoanDetails;
import com.tcs.finalproject.bankapp.service.LoanDetailsService;

@RestController
public class LoanDetailsController {
    private final LoanDetailsService loanDetailsService;

    public LoanDetailsController(LoanDetailsService loanDetailsService) {
        this.loanDetailsService = loanDetailsService;
    }

    @GetMapping("/loandetails")
    public List<LoanDetails> getAllLoanDetails() {
        return loanDetailsService.getAllLoanDetails();
    }

    @GetMapping("/loandetails/{id}")
    public LoanDetails getLoanDetailsById(@PathVariable Long id) {
        return loanDetailsService.getLoanDetailsById(id);
    }

    @PostMapping("/loandetails")
    public LoanDetails saveOrUpdateAccount(@RequestBody LoanDetails loanDet) {
        return loanDetailsService.saveOrUpdateLoanDetails(loanDet);
    }

    @DeleteMapping("/loandetails/{id}")
    public void deleteAccount(@PathVariable Long id) {
        loanDetailsService.deleteLoanDetails(id);
    }
}
