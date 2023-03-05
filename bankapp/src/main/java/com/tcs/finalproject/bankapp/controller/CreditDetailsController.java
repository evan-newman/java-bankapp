package com.tcs.finalproject.bankapp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.finalproject.bankapp.entity.CreditDetails;
import com.tcs.finalproject.bankapp.service.CreditDetailsService;

@RestController
public class CreditDetailsController {
    private final CreditDetailsService creditDetailsService;

    public CreditDetailsController(CreditDetailsService creditDetailsService) {
        this.creditDetailsService = creditDetailsService;
    }

    @GetMapping("/creditdetails")
    public List<CreditDetails> getAllCreditDetails() {
        return creditDetailsService.getAllCreditDetails();
    }

    @GetMapping("/creditdetails/{id}")
    public CreditDetails getCreditDetailsById(@PathVariable Long id) {
        return creditDetailsService.getCreditDetailsById(id);
    }

    @PostMapping("/creditdetails")
    public CreditDetails saveOrUpdateAccount(@RequestBody CreditDetails ccDet) {
        return creditDetailsService.saveOrUpdateCreditDetails(ccDet);
    }

    @DeleteMapping("/creditdetails/{id}")
    public void deleteAccount(@PathVariable Long id) {
        creditDetailsService.deleteCreditDetails(id);
    }
}
