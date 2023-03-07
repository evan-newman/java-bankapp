package com.tcs.finalproject.bankapp.service;

import java.util.List;
import java.util.Optional;
import java.sql.Timestamp;

import org.springframework.stereotype.Service;

import com.tcs.finalproject.bankapp.entity.AccountEntries;
import com.tcs.finalproject.bankapp.entity.Accounts;
import com.tcs.finalproject.bankapp.entity.CreditDetails;
import com.tcs.finalproject.bankapp.entity.LoanDetails;
import com.tcs.finalproject.bankapp.model.CreditDetailsDTO;
import com.tcs.finalproject.bankapp.model.LoanDetailsDTO;
import com.tcs.finalproject.bankapp.repository.AccountEntriesRepository;
import com.tcs.finalproject.bankapp.repository.AccountsRepository;
import com.tcs.finalproject.bankapp.repository.CreditDetailsRepository;
import com.tcs.finalproject.bankapp.repository.LoanDetailsRepository;

import com.tcs.finalproject.bankapp.exception.BankException;

@Service
public class AccountsService {
    private final AccountsRepository accountsRepo;
    private final AccountEntriesRepository accountEntriesRepo;
    private final CreditDetailsRepository creditDetailsRepo;
    private final LoanDetailsRepository loanDetailsRepo;

    public AccountsService(AccountsRepository accountsRepo, 
                           AccountEntriesRepository accountEntriesRepo, 
                           CreditDetailsRepository creditDetailsRepo,
                           LoanDetailsRepository loanDetailsRepo) {
        this.accountsRepo = accountsRepo;
        this.accountEntriesRepo = accountEntriesRepo;
        this.creditDetailsRepo = creditDetailsRepo;
        this.loanDetailsRepo = loanDetailsRepo;
    }

    public List<Accounts> getAllAccounts() {
        return accountsRepo.findAll();
    }

    public Accounts getAccountById(Long id) {
        return accountsRepo.findById(id).get();
    }

    public Accounts getAccountByIdAndOpen(Long id) {
        return accountsRepo.findByIdAndOpen(id, true);
    }

    public Accounts saveOrUpdateAccount(Accounts account) {
        return accountsRepo.save(account);
    }

    public void deleteAccount(Long id) {
        accountsRepo.deleteById(id);
    } 
    // -----------------------ADMIN----------------------------

    public Accounts closeAccountById(Long id) throws BankException {
        Optional<Accounts> accOpt = accountsRepo.findById(id);
        if (!accOpt.isPresent()) {
            throw new BankException("Account is not present");
        }

        Accounts acc = accOpt.get();

        acc.setOpen(false);
        accountsRepo.saveAndFlush(acc);
        return acc;
    }

    public Accounts updateAccount(Accounts acc) throws BankException {
        Optional<Accounts> newAccOpt = accountsRepo.findById(acc.getId());
        if (!newAccOpt.isPresent()) {
            throw new BankException("Account is not present");
        }

        return accountsRepo.save(acc);
    }

    public CreditDetails createCreditAccount(Long userId, CreditDetailsDTO credDTO) throws BankException {
        if (credDTO.getCreditLimit() >= 0) {
            throw new BankException("Incorrect credit limit");
        }

        Accounts newAcc = accountsRepo.save(new Accounts(credDTO, userId));

        AccountEntries newAccEnt = accountEntriesRepo.save(new AccountEntries(0L, newAcc.getId(), 0, new Timestamp(System.currentTimeMillis())));

        CreditDetails newCredDet = creditDetailsRepo.save(new CreditDetails(credDTO, newAcc.getId()));

        return newCredDet;
    }

    public LoanDetails createLoanAccount(Long userId, LoanDetailsDTO loanDTO) throws BankException {
        if (loanDTO.getOriginalAmount() >= 0) {
            throw new BankException("Incorrect amount given");
        }

        Accounts newAcc = accountsRepo.save(new Accounts(loanDTO, userId));

        AccountEntries newAccEnt = accountEntriesRepo.save(new AccountEntries(0L, newAcc.getId(), loanDTO.getOriginalAmount(), new Timestamp(System.currentTimeMillis())));

        LoanDetails newLoanDet = loanDetailsRepo.save(new LoanDetails(loanDTO, newAcc.getId()));

        return newLoanDet;
    }

    // -----------------------USER-----------------------------

    public List<Accounts> getAllUsersOpenAccounts(Long id) {
        return accountsRepo.findByUserIdAndOpen(id, true);
    }

    public Accounts getUsersOpenAccountById(Long id, Long userId) {
        return accountsRepo.findByIdAndUserIdAndOpen(id, userId, true);
    }

    public double getUsersOpenAccountBalance(Long userId, Long accId) throws BankException {
        Accounts acc = accountsRepo.findByIdAndUserIdAndOpen(accId, userId, true);
        if (acc == null) {
            throw new BankException("Account is either closed or not owned by the user");
        }
            
        AccountEntries accEnt = accountEntriesRepo.findByAccountId(accId);
        if (accEnt == null) {
            throw new BankException("Account not found");
        }
        return accEnt.getAmount();
    }

    public void makeAccountDeposit(Long accId, Long userId, double depositAmount) throws BankException {
        Accounts acc = getUsersOpenAccountById(accId, userId);
        if (acc == null) {
            throw new BankException("Account not found");
        }

        Long accountId = acc.getAccountTypeId();
        //only checking and savings can make deposit
        if (accountId == 1 || accountId == 2) {
            AccountEntries accEnt = accountEntriesRepo.findByAccountId(accId);
            double newAmount = accEnt.getAmount() + depositAmount;
            accEnt.setAmount(newAmount);
            accountEntriesRepo.saveAndFlush(accEnt);
        } else { //loan or cc account
            throw new BankException("Attempting deposit on a non checking or savings account");
        }
    }

    public void makeAccountWithdrawal(Long accId, Long userId, double withdrawAmount) throws BankException {
        Accounts acc = getUsersOpenAccountById(accId, userId);
        if (acc == null) {
            throw new BankException("Account not found");
        }

        Long accountId = acc.getAccountTypeId();
        //only checking and savings can make withdrawal
        if (accountId == 1 || accountId == 2) {
            AccountEntries accEnt = accountEntriesRepo.findByAccountId(accId);
            double newAmount = accEnt.getAmount() - withdrawAmount;
            if (newAmount < 0) {
                throw new BankException("invalid amount, sets account to negative balance");
            }

            accEnt.setAmount(newAmount);
            accountEntriesRepo.saveAndFlush(accEnt);
        } else { //loan or cc account
            throw new BankException("Attempting withdrawal on a non checking or savings account");
        }
    }

    public void makeCreditCardPurchase(Long accId, Long userId, double purchaseAmount) throws BankException {
        Accounts acc = getUsersOpenAccountById(accId, userId);
        if (acc == null) {
            throw new BankException("Account not found");
        }

        Long accountId = acc.getAccountTypeId();
        // only credit cards can you make a purchase
        if (accountId != 4) {
            throw new BankException("Attempting purchase on account that's not a credit card");
        }
        AccountEntries accEnt = accountEntriesRepo.findByAccountId(accId);
        CreditDetails credDet = creditDetailsRepo.findByAccountId(accId);

        double newAmount = accEnt.getAmount() - purchaseAmount;
        double maxLimit = credDet.getCreditLimit();
        if (newAmount < maxLimit) {
            throw new BankException("invalid amount, sets account pass max credit limit");
        }
        accEnt.setAmount(newAmount);
        accountEntriesRepo.saveAndFlush(accEnt);
    }

    public void makeAccountPayment(Long accId, Long userId, double paymentAmount) throws BankException {
        Accounts acc = getUsersOpenAccountById(accId, userId);
        if (acc == null) {
            throw new BankException("Account not found");
        }

        Long accountId = acc.getAccountTypeId();
        //only credit cards and loans can make a payment
        if (accountId == 3 || accountId == 4) {
            AccountEntries accEnt = accountEntriesRepo.findByAccountId(accId);
            double newAmount = accEnt.getAmount() + paymentAmount;
            if (newAmount > 0) {
                throw new BankException("invalid amount, sets account to positive balance");
            } else if (accountId == 3 && newAmount == 0) { //loan payed off close account
                acc.setOpen(false);
                accountsRepo.saveAndFlush(acc);
            }

            accEnt.setAmount(newAmount);
            accountEntriesRepo.saveAndFlush(accEnt);
        } else { //checking or savings account
            throw new BankException("Attempting payment on a non credit card or loan account");
        }
    }

    public void makeTransfer(Long fromAccId, Long toAccId, Long userId, double transferAmount) throws BankException {
        Accounts fromAcc = getUsersOpenAccountById(fromAccId, userId);
        if (fromAcc == null) {
            throw new BankException("From account not found");
        }

        Long fromAccountType = fromAcc.getAccountTypeId();
        // cannot transfer FROM cc or loan account
        if (fromAccountType == 3 || fromAccountType == 4) {
            throw new BankException("Cannot transfer from a credit card or loan account");
        }

        Accounts toAcc = getAccountByIdAndOpen(toAccId);
        if (toAcc == null) {
            throw new BankException("To account not found");
        }
        Long toAccountType = toAcc.getAccountTypeId();

        //withdrawal
        AccountEntries fromAccEnt = accountEntriesRepo.findByAccountId(fromAccId);
        double fromNewAmount = fromAccEnt.getAmount() - transferAmount;
        if (fromNewAmount < 0) {
            throw new BankException("invalid amount, sets from account transfer to negative balance");
        }

        AccountEntries toAccEnt = accountEntriesRepo.findByAccountId(toAccId);
        double toNewAmount = toAccEnt.getAmount() + transferAmount;
        if (toAccountType == 3 || toAccountType == 4) {
            if (toNewAmount > 0) {
                throw new BankException("invalid amount, sets to account transfer to positive balance");
            } else if (toAccountType == 3 && toNewAmount == 0) { //loan payed off close account
                toAcc.setOpen(false);
                accountsRepo.saveAndFlush(toAcc);
            }

        }

        fromAccEnt.setAmount(fromNewAmount);
        accountEntriesRepo.saveAndFlush(fromAccEnt);
        toAccEnt.setAmount(toNewAmount);
        accountEntriesRepo.saveAndFlush(toAccEnt);
    }
}
