package com.tcs.finalproject.bankapp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.tcs.finalproject.bankapp.entity.Accounts;
import com.tcs.finalproject.bankapp.entity.AccountEntries;
import com.tcs.finalproject.bankapp.entity.CreditDetails;
import com.tcs.finalproject.bankapp.entity.LoanDetails;
import com.tcs.finalproject.bankapp.model.CreditDetailsDTO;
import com.tcs.finalproject.bankapp.model.LoanDetailsDTO;
import com.tcs.finalproject.bankapp.repository.AccountEntriesRepository;
import com.tcs.finalproject.bankapp.repository.AccountsRepository;
import com.tcs.finalproject.bankapp.repository.CreditDetailsRepository;
import com.tcs.finalproject.bankapp.repository.LoanDetailsRepository;

import com.tcs.finalproject.bankapp.exception.BankException;


/*
*******************************************************************************************************************
            READ ME!!!!!!!!
            you must adjust the applications.properties to have ddl-auto set to 'create-drop' if you wish to run tests!!
*******************************************************************************************************************
*/

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdminServiceTests {
    @Autowired
    private AccountsRepository accountsRepo;

    @Autowired
    private AccountEntriesRepository accountEntRepo;

    @Autowired
    private CreditDetailsRepository credDetRepo;

    @Autowired
    private LoanDetailsRepository loanDetRepo;

    @Autowired
    private AccountsService accountService;

    @Autowired
    private CreditDetailsService creditDetailsService;

    @Autowired
    private LoanDetailsService loanDetailsService;

/*
*******************************************************************************************************************
            READ ME!!!!!!!!
            you must adjust the applications.properties to have ddl-auto set to 'create-drop' if you wish to run tests!!
*******************************************************************************************************************
*/

    //-----------------------------------CLOSE ACCOUNT-----------------------------------------------------
    @Test
    public void closeAccount() throws Exception {
        Accounts acc = new Accounts();
        acc.setUserId(1L);
        acc.setAccountTypeId(1L);
        acc.setOpen(true);
        accountsRepo.saveAndFlush(acc);

        accountService.closeAccountById(1L);

        Accounts closeAcc = accountsRepo.findById(1L).get();
        assertEquals(closeAcc.getOpen(), false, "account should have been closed but is open");

    } 

    //---------------------------------------UPDATE CREDIT ACCOUNT----------------------------------------
    @Test
    public void correctUpdateCreditAccount() throws Exception {
        Accounts acc = new Accounts();
        acc.setUserId(1L);
        acc.setAccountTypeId(4L);
        acc.setOpen(true);
 
        AccountEntries accEnt = new AccountEntries();
        accEnt.setAccountId(1L);
        accEnt.setAmount(-500);

        CreditDetails credDet = new CreditDetails();
        credDet.setAccountId(1L);
        credDet.setCreditLimit(-1000);
        credDet.setCcv(666);

        accountsRepo.saveAndFlush(acc);
        accountEntRepo.saveAndFlush(accEnt);
        CreditDetails credNewDet = credDetRepo.saveAndFlush(credDet);

        credNewDet.setCcv(666);

        creditDetailsService.updateCreditDetails(credNewDet);

        CreditDetails updateCredDet = credDetRepo.findById(1L).get();
        assertEquals(updateCredDet.getCcv(), credNewDet.getCcv(), "account should have updated");

    }

    @Test
    public void incorrectAccountForUpdateCredit() throws Exception {
        Accounts acc = new Accounts();
        acc.setUserId(1L);
        acc.setAccountTypeId(2L);
        acc.setOpen(true);
 
        AccountEntries accEnt = new AccountEntries();
        accEnt.setAccountId(1L);
        accEnt.setAmount(-500);

        CreditDetails credDet = new CreditDetails();
        credDet.setAccountId(1L);
        credDet.setCreditLimit(-1000);
        credDet.setCcv(666);

        accountsRepo.saveAndFlush(acc);
        accountEntRepo.saveAndFlush(accEnt);
        CreditDetails credNewDet = credDetRepo.saveAndFlush(credDet);

        credNewDet.setCcv(666);

        Exception exception = assertThrows(BankException.class, () -> creditDetailsService.updateCreditDetails(credNewDet));
        assertEquals("Incorrect account type not a credit card", exception.getMessage());
    }

    @Test
    public void incorrectAmountForUpdateCredit() throws Exception {
        Accounts acc = new Accounts();
        acc.setUserId(1L);
        acc.setAccountTypeId(4L);
        acc.setOpen(true);
 
        AccountEntries accEnt = new AccountEntries();
        accEnt.setAccountId(1L);
        accEnt.setAmount(-1000);

        CreditDetails credDet = new CreditDetails();
        credDet.setAccountId(1L);
        credDet.setCreditLimit(-500);
        credDet.setCcv(666);

        accountsRepo.saveAndFlush(acc);
        accountEntRepo.saveAndFlush(accEnt);
        CreditDetails credNewDet = credDetRepo.saveAndFlush(credDet);

        credNewDet.setCcv(666);

        Exception exception = assertThrows(BankException.class, () -> creditDetailsService.updateCreditDetails(credNewDet));
        assertEquals("Invalid amount sets amount past credit limit", exception.getMessage());
    }

    //---------------------------------------UPDATE LOAN ACCOUNT----------------------------------------
    @Test
    public void correctUpdateAccountLoan() throws Exception {
        Accounts acc = new Accounts();
        acc.setUserId(1L);
        acc.setAccountTypeId(3L);
        acc.setOpen(true);
 
        AccountEntries accEnt = new AccountEntries();
        accEnt.setAccountId(1L);
        accEnt.setAmount(-500);

        LoanDetails loanDet = new LoanDetails();
        loanDet.setAccountId(1L);
        loanDet.setOriginalAmount(-5000);

        accountsRepo.saveAndFlush(acc);
        accountEntRepo.saveAndFlush(accEnt);
        LoanDetails loanNewDet = loanDetRepo.saveAndFlush(loanDet);

        loanNewDet.setOriginalAmount(-4000);

        loanDetailsService.updateLoanDetails(loanNewDet);

        LoanDetails updateLoanDet = loanDetRepo.findById(1L).get();
        assertEquals((int) updateLoanDet.getOriginalAmount(), (int) loanNewDet.getOriginalAmount(), "account should have updated");

    }

    @Test
    public void incorrectAccountForUpdateLoan() throws Exception {
        Accounts acc = new Accounts();
        acc.setUserId(1L);
        acc.setAccountTypeId(4L);
        acc.setOpen(true);
 
        AccountEntries accEnt = new AccountEntries();
        accEnt.setAccountId(1L);
        accEnt.setAmount(-500);

        LoanDetails loanDet = new LoanDetails();
        loanDet.setAccountId(1L);
        loanDet.setOriginalAmount(-5000);

        accountsRepo.saveAndFlush(acc);
        accountEntRepo.saveAndFlush(accEnt);
        LoanDetails loanNewDet = loanDetRepo.saveAndFlush(loanDet);

        loanNewDet.setOriginalAmount(-6000);

        Exception exception = assertThrows(BankException.class, () -> loanDetailsService.updateLoanDetails(loanNewDet));
        assertEquals("Incorrect account type not a loan", exception.getMessage());
    }

    @Test
    public void incorrectAmountForUpdateLoan() throws Exception {
        Accounts acc = new Accounts();
        acc.setUserId(1L);
        acc.setAccountTypeId(3L);
        acc.setOpen(true);
 
        AccountEntries accEnt = new AccountEntries();
        accEnt.setAccountId(1L);
        accEnt.setAmount(-500);

        LoanDetails loanDet = new LoanDetails();
        loanDet.setAccountId(1L);
        loanDet.setOriginalAmount(-5000);

        accountsRepo.saveAndFlush(acc);
        accountEntRepo.saveAndFlush(accEnt);
        LoanDetails loanNewDet = loanDetRepo.saveAndFlush(loanDet);

        loanNewDet.setOriginalAmount(-6000);

        Exception exception = assertThrows(BankException.class, () -> loanDetailsService.updateLoanDetails(loanNewDet));
        assertEquals("Not permitted to adjust the original loan amount to a greater amount", exception.getMessage());
    }

    //--------------------------------------CREATE CREDIT ACCOUNT------------------------------------------
    @Test
    public void correctCreditAccountCreation() throws Exception {
        CreditDetailsDTO credDetDTO = new CreditDetailsDTO("Credit Account", -1000, "1234123412341234", 4, 2027, 223);

        CreditDetails newCreditAccount = accountService.createCreditAccount(1L, credDetDTO);


        CreditDetails insertedCredDet = credDetRepo.findById(1L).get();
        assertEquals(insertedCredDet, newCreditAccount, "account saved should have the same values as returned");
    }

    @Test
    public void incorrectCreditAccountCreation() throws Exception {
        CreditDetailsDTO credDetDTO = new CreditDetailsDTO("Credit Account", 1000, "1234123412341234", 4, 2027, 223);

        Exception exception = assertThrows(BankException.class, () -> accountService.createCreditAccount(1L, credDetDTO));
        assertEquals("Incorrect credit limit", exception.getMessage());
    }

    //--------------------------------------CREATE CREDIT ACCOUNT------------------------------------------
    @Test
    public void correctLoanAccountCreation() throws Exception {
        LoanDetailsDTO loanDetDTO = new LoanDetailsDTO("Loan Account", -20000, 60, 0.05);

        LoanDetails newLoanAccount = accountService.createLoanAccount(1L, loanDetDTO);


        LoanDetails insertedLoanDet = loanDetRepo.findById(1L).get();
        assertEquals(insertedLoanDet, newLoanAccount, "account saved should have the same values as returned");
        assertEquals((int) insertedLoanDet.getMonthlyPayment(), 416, "interest rate calculated incorrectly");
    }

    @Test
    public void incorrectLoanAccountCreation() throws Exception {
        LoanDetailsDTO loanDetDTO = new LoanDetailsDTO("Loan Account", 1000, 48, 0.05);

        Exception exception = assertThrows(BankException.class, () -> accountService.createLoanAccount(1L, loanDetDTO));
        assertEquals("Incorrect loan amount given", exception.getMessage());
    }

}
