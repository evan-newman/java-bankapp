package com.tcs.finalproject.bankapp.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.springframework.test.annotation.DirtiesContext;

import com.tcs.finalproject.bankapp.entity.AccountEntries;
import com.tcs.finalproject.bankapp.entity.Accounts;
import com.tcs.finalproject.bankapp.entity.CreditDetails;
import com.tcs.finalproject.bankapp.entity.LoanDetails;
import com.tcs.finalproject.bankapp.exception.BankException;
import com.tcs.finalproject.bankapp.repository.AccountsRepository;
import com.tcs.finalproject.bankapp.repository.AccountEntriesRepository;
import com.tcs.finalproject.bankapp.repository.CreditDetailsRepository;
import com.tcs.finalproject.bankapp.repository.LoanDetailsRepository;

/*
*******************************************************************************************************************
            READ ME!!!!!!!!
            you must adjust the applications.properties to have ddl-auto set to 'create-drop' if you wish to run tests!!
*******************************************************************************************************************
*/

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceTests {
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

/*
*******************************************************************************************************************
            READ ME!!!!!!!!
            you must adjust the applications.properties to have ddl-auto set to 'create-drop' if you wish to run tests!!
*******************************************************************************************************************
*/

    //----------------------------CHECKINGS/SAVINSG DEPOSIT---------------------------------------
    @Test
    public void correctDeposit() throws Exception {
        Accounts acc = new Accounts();
        acc.setUserId(1L);
        acc.setAccountTypeId(1L);
        acc.setOpen(true);

        AccountEntries accEnt = new AccountEntries();
        accEnt.setAccountId(1L);
        accEnt.setAmount(500);

        accountsRepo.saveAndFlush(acc);
        accountEntRepo.saveAndFlush(accEnt);

        accountService.makeAccountDeposit(1L, 1L, 100);

        AccountEntries depositEnt = accountEntRepo.findById(1L).get();
        assertEquals((int) depositEnt.getAmount(), 600, "deposit did not change amount correctly");
    }

    @Test
    public void incorrectAccountForDeposit() throws Exception {
        Accounts acc = new Accounts();
        acc.setUserId(1L);
        acc.setAccountTypeId(3L);
        acc.setOpen(true);

        AccountEntries accEnt = new AccountEntries();
        accEnt.setAccountId(1L);
        accEnt.setAmount(500);

        accountsRepo.saveAndFlush(acc);
        accountEntRepo.saveAndFlush(accEnt);

        Exception exception = assertThrows(BankException.class, () -> accountService.makeAccountDeposit(1L, 1L, 100));
        assertEquals("Attempting deposit on a non checking or savings account", exception.getMessage());
    }

    //------------------------------CHECKING/SAVINGS WITHDRAWAL-----------------------------------
    @Test
    public void correctWithdrawal() throws Exception {
        Accounts acc = new Accounts();
        acc.setUserId(1L);
        acc.setAccountTypeId(1L);
        acc.setOpen(true);

        AccountEntries accEnt = new AccountEntries();
        accEnt.setAccountId(1L);
        accEnt.setAmount(500);

        accountsRepo.saveAndFlush(acc);
        accountEntRepo.saveAndFlush(accEnt);

        accountService.makeAccountWithdrawal(1L, 1L, 100);

        AccountEntries depositEnt = accountEntRepo.findById(1L).get();
        assertEquals((int) depositEnt.getAmount(), 400, "withdrawal did not change amount correctly");
    }

    @Test
    public void incorrectAccountForWithdrawal() throws Exception {
        Accounts acc = new Accounts();
        acc.setUserId(1L);
        acc.setAccountTypeId(3L);
        acc.setOpen(true);

        AccountEntries accEnt = new AccountEntries();
        accEnt.setAccountId(1L);
        accEnt.setAmount(500);

        accountsRepo.saveAndFlush(acc);
        accountEntRepo.saveAndFlush(accEnt);

        Exception exception = assertThrows(BankException.class, () -> accountService.makeAccountWithdrawal(1L, 1L, 100));
        assertEquals("Attempting withdrawal on a non checking or savings account", exception.getMessage());
    }

    @Test
    public void incorrectAmountForWithdrawal() throws Exception {
        Accounts acc = new Accounts();
        acc.setUserId(1L);
        acc.setAccountTypeId(2L);
        acc.setOpen(true);

        AccountEntries accEnt = new AccountEntries();
        accEnt.setAccountId(1L);
        accEnt.setAmount(500);

        accountsRepo.saveAndFlush(acc);
        accountEntRepo.saveAndFlush(accEnt);

        Exception exception = assertThrows(BankException.class, () -> accountService.makeAccountWithdrawal(1L, 1L, 600));
        assertEquals("invalid amount, sets account to negative balance", exception.getMessage());
    }

    //--------------------------CREDIT CARD PURCHASE----------------------------------------------
    @Test
    public void correctPurchase() throws Exception {
        
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

        accountsRepo.saveAndFlush(acc);
        accountEntRepo.saveAndFlush(accEnt);
        credDetRepo.saveAndFlush(credDet);

        accountService.makeCreditCardPurchase(1L, 1L, 100);

        AccountEntries updateEnt = accountEntRepo.findById(1L).get();
        assertEquals((int) updateEnt.getAmount(), -600, "purhcase did not change amount correctly");
    }

    @Test
    public void incorrectAmountForPurchase() throws Exception{
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

        accountsRepo.saveAndFlush(acc);
        accountEntRepo.saveAndFlush(accEnt);
        credDetRepo.saveAndFlush(credDet);

        Exception exception = assertThrows(BankException.class, () -> accountService.makeCreditCardPurchase(1L, 1L, 2000));
        assertEquals("invalid amount, sets account pass max credit limit", exception.getMessage());
        
    }

    @Test
    public void incorrectAccountForPurchase() throws Exception {
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
        loanDetRepo.saveAndFlush(loanDet);

        Exception exception = assertThrows(BankException.class, () -> accountService.makeCreditCardPurchase(1L, 1L, 100));
        assertEquals("Attempting purchase on account that's not a credit card", exception.getMessage());

    } 

    //-------------------------------CREDIT CARD/LOAN PAYMENT-----------------------------------

    @Test
    public void correctPayment() throws Exception {
        
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
        loanDetRepo.saveAndFlush(loanDet);

        accountService.makeAccountPayment(1L, 1L, 100);

        AccountEntries updateEnt = accountEntRepo.findById(1L).get();
        assertEquals((int) updateEnt.getAmount(), -400, "payment did not change amount correctly");
    }

    @Test
    public void correctPaymentClosesLoanAccount() throws Exception {
        
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
        loanDetRepo.saveAndFlush(loanDet);

        accountService.makeAccountPayment(1L, 1L, 500);

        Accounts accClose = accountsRepo.findById(1L).get();
        assertEquals(accClose.getOpen(), false, "loan should have been closed after paying off amount");
    }

    @Test
    public void incorrectAccountForPayment() throws Exception {
        Accounts acc = new Accounts();
        acc.setUserId(1L);
        acc.setAccountTypeId(1L);
        acc.setOpen(true);

        AccountEntries accEnt = new AccountEntries();
        accEnt.setAccountId(1L);
        accEnt.setAmount(-500);

        accountsRepo.saveAndFlush(acc);
        accountEntRepo.saveAndFlush(accEnt);

        Exception exception = assertThrows(BankException.class, () -> accountService.makeAccountPayment(1L, 1L, 100));
        assertEquals("Attempting payment on a non credit card or loan account", exception.getMessage());
    }

    @Test
    public void incorrectAmountForPayment() throws Exception {
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
        loanDetRepo.saveAndFlush(loanDet);

        Exception exception = assertThrows(BankException.class, () -> accountService.makeAccountPayment(1L, 1L, 6000));
        assertEquals("invalid amount, sets account to positive balance", exception.getMessage());
    }

    //----------------------------------ACCOUNT TRANSFER---------------------------------------
    @Test
    public void correctAccountsTransfer() throws Exception {
        Accounts fromAcc = new Accounts();
        fromAcc.setUserId(1L);
        fromAcc.setAccountTypeId(1L);
        fromAcc.setOpen(true);

        AccountEntries fromAccEnt = new AccountEntries();
        fromAccEnt.setAccountId(1L);
        fromAccEnt.setAmount(500);

        accountsRepo.saveAndFlush(fromAcc);
        accountEntRepo.saveAndFlush(fromAccEnt);

        Accounts toAcc = new Accounts();
        toAcc.setUserId(1L);
        toAcc.setAccountTypeId(3L);
        toAcc.setOpen(true);

        AccountEntries toAccEnt = new AccountEntries();
        toAccEnt.setAccountId(2L);
        toAccEnt.setAmount(-500);

        LoanDetails loanDet = new LoanDetails();
        loanDet.setAccountId(2L);
        loanDet.setOriginalAmount(-5000);

        accountsRepo.saveAndFlush(toAcc);
        accountEntRepo.saveAndFlush(toAccEnt);
        loanDetRepo.saveAndFlush(loanDet);

        accountService.makeTransfer(1L, 2L, 1L, 100);

        AccountEntries updatedFromAccEnt = accountEntRepo.findById(1L).get();
        assertEquals((int) updatedFromAccEnt.getAmount(), 400, "transfer did not change from account amount correctly");

        AccountEntries updatedToAccEnt = accountEntRepo.findById(2L).get();
        assertEquals((int) updatedToAccEnt.getAmount(), -400, "transfer did not change to account amount correctly");

    }

    @Test
    public void correctAccountsTransferClosesLoan() throws Exception {
        Accounts fromAcc = new Accounts();
        fromAcc.setUserId(1L);
        fromAcc.setAccountTypeId(1L);
        fromAcc.setOpen(true);

        AccountEntries fromAccEnt = new AccountEntries();
        fromAccEnt.setAccountId(1L);
        fromAccEnt.setAmount(500);

        accountsRepo.saveAndFlush(fromAcc);
        accountEntRepo.saveAndFlush(fromAccEnt);

        Accounts toAcc = new Accounts();
        toAcc.setUserId(1L);
        toAcc.setAccountTypeId(3L);
        toAcc.setOpen(true);

        AccountEntries toAccEnt = new AccountEntries();
        toAccEnt.setAccountId(2L);
        toAccEnt.setAmount(-500);

        LoanDetails loanDet = new LoanDetails();
        loanDet.setAccountId(2L);
        loanDet.setOriginalAmount(-5000);

        accountsRepo.saveAndFlush(toAcc);
        accountEntRepo.saveAndFlush(toAccEnt);
        loanDetRepo.saveAndFlush(loanDet);

        accountService.makeTransfer(1L, 2L, 1L, 500);

        Accounts accClose = accountsRepo.findById(2L).get();
        assertEquals(accClose.getOpen(), false, "loan should have been closed after paying off amount");
    }

    @Test
    public void incorrectFromAccountsTransfer() throws Exception {
        Accounts fromAcc = new Accounts();
        fromAcc.setUserId(1L);
        fromAcc.setAccountTypeId(4L);
        fromAcc.setOpen(true);

        AccountEntries fromAccEnt = new AccountEntries();
        fromAccEnt.setAccountId(1L);
        fromAccEnt.setAmount(500);

        accountsRepo.saveAndFlush(fromAcc);
        accountEntRepo.saveAndFlush(fromAccEnt);

        Accounts toAcc = new Accounts();
        toAcc.setUserId(1L);
        toAcc.setAccountTypeId(3L);
        toAcc.setOpen(true);

        AccountEntries toAccEnt = new AccountEntries();
        toAccEnt.setAccountId(2L);
        toAccEnt.setAmount(-500);

        LoanDetails loanDet = new LoanDetails();
        loanDet.setAccountId(2L);
        loanDet.setOriginalAmount(-5000);

        accountsRepo.saveAndFlush(toAcc);
        accountEntRepo.saveAndFlush(toAccEnt);
        loanDetRepo.saveAndFlush(loanDet);

        Exception exception = assertThrows(BankException.class, () -> accountService.makeTransfer(1L, 2L, 1L, 100));
        assertEquals("Cannot transfer from a credit card or loan account", exception.getMessage());

    }

    @Test
    public void incorrectAmountFromAccountForTransfer() throws Exception {
        Accounts fromAcc = new Accounts();
        fromAcc.setUserId(1L);
        fromAcc.setAccountTypeId(2L);
        fromAcc.setOpen(true);

        AccountEntries fromAccEnt = new AccountEntries();
        fromAccEnt.setAccountId(1L);
        fromAccEnt.setAmount(500);

        accountsRepo.saveAndFlush(fromAcc);
        accountEntRepo.saveAndFlush(fromAccEnt);

        Accounts toAcc = new Accounts();
        toAcc.setUserId(1L);
        toAcc.setAccountTypeId(3L);
        toAcc.setOpen(true);

        AccountEntries toAccEnt = new AccountEntries();
        toAccEnt.setAccountId(2L);
        toAccEnt.setAmount(-1000);

        LoanDetails loanDet = new LoanDetails();
        loanDet.setAccountId(2L);
        loanDet.setOriginalAmount(-5000);

        accountsRepo.saveAndFlush(toAcc);
        accountEntRepo.saveAndFlush(toAccEnt);
        loanDetRepo.saveAndFlush(loanDet);

        Exception exception = assertThrows(BankException.class, () -> accountService.makeTransfer(1L, 2L, 1L, 600));
        assertEquals("invalid amount, sets from account transfer to negative balance", exception.getMessage());

    }

    @Test
    public void incorrectAmountToAccountForTransfer() throws Exception {
        Accounts fromAcc = new Accounts();
        fromAcc.setUserId(1L);
        fromAcc.setAccountTypeId(2L);
        fromAcc.setOpen(true);

        AccountEntries fromAccEnt = new AccountEntries();
        fromAccEnt.setAccountId(1L);
        fromAccEnt.setAmount(500);

        accountsRepo.saveAndFlush(fromAcc);
        accountEntRepo.saveAndFlush(fromAccEnt);

        Accounts toAcc = new Accounts();
        toAcc.setUserId(1L);
        toAcc.setAccountTypeId(3L);
        toAcc.setOpen(true);

        AccountEntries toAccEnt = new AccountEntries();
        toAccEnt.setAccountId(2L);
        toAccEnt.setAmount(-300);

        LoanDetails loanDet = new LoanDetails();
        loanDet.setAccountId(2L);
        loanDet.setOriginalAmount(-5000);

        accountsRepo.saveAndFlush(toAcc);
        accountEntRepo.saveAndFlush(toAccEnt);
        loanDetRepo.saveAndFlush(loanDet);

        Exception exception = assertThrows(BankException.class, () -> accountService.makeTransfer(1L, 2L, 1L, 500));
        assertEquals("invalid amount, sets to account transfer to positive balance", exception.getMessage());

    }

}
