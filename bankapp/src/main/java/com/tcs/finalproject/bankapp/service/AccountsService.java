package com.tcs.finalproject.bankapp.service;

import java.util.List;
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

    public Accounts closeAccountById(Long id) {
        Accounts acc = accountsRepo.findById(id).get();
        if (acc == null) {
            //error here
            return null;
        }

        acc.setOpen(false);
        accountsRepo.saveAndFlush(acc);
        return acc;
    }

    public Accounts updateAccount(Accounts acc) {
        Accounts newAcc = accountsRepo.findById(acc.getId()).get();
        if (newAcc == null) {
            // error here
            return null;
        }

        return accountsRepo.save(acc);
    }

    public CreditDetails createCreditAccount(Long userId, CreditDetailsDTO credDTO) {
        if (credDTO.getCreditLimit() >= 0) {
            //error here
            return null;
        }

        Accounts newAcc = accountsRepo.save(new Accounts(credDTO, userId));

        AccountEntries newAccEnt = accountEntriesRepo.save(new AccountEntries(0L, newAcc.getId(), 0, new Timestamp(System.currentTimeMillis())));

        CreditDetails newCredDet = creditDetailsRepo.save(new CreditDetails(credDTO, newAcc.getId()));

        return newCredDet;
    }

    public LoanDetails createLoanAccount(Long userId, LoanDetailsDTO loanDTO) {
        if (loanDTO.getOriginalAmount() >= 0) {
            //error here
            return null;
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

    public double getUsersOpenAccountBalance(Long userId, Long accId) {
        Accounts acc = accountsRepo.findByIdAndUserIdAndOpen(accId, userId, true);
        if (acc == null) {
            // error here
            // possibly account is closed, or not owned by user
            return 0;
        }
            
        AccountEntries accEnt = accountEntriesRepo.findByAccountId(accId);
        if (accEnt == null) {
            // error here
            return 0;
        }
        return accEnt.getAmount();
    }

    public void makeAccountDeposit(Long accId, Long userId, double depositAmount) {
        Accounts acc = getUsersOpenAccountById(accId, userId);
        if (acc == null) {
            //error here
            return;
        }

        Long accountId = acc.getAccountTypeId();
        //only checking and savings can make deposit
        if (accountId == 1 || accountId == 2) {
            AccountEntries accEnt = accountEntriesRepo.findByAccountId(accId);
            double newAmount = accEnt.getAmount() + depositAmount;
            accEnt.setAmount(newAmount);
            accountEntriesRepo.saveAndFlush(accEnt);
        } else { //loan or cc account
            //error here
            return;
        }
    }

    public void makeAccountWithdrawal(Long accId, Long userId, double withdrawAmount) {
        Accounts acc = getUsersOpenAccountById(accId, userId);
        if (acc == null) {
            //error here
            return;
        }

        Long accountId = acc.getAccountTypeId();
        //only checking and savings can make withdrawal
        if (accountId == 1 || accountId == 2) {
            AccountEntries accEnt = accountEntriesRepo.findByAccountId(accId);
            double newAmount = accEnt.getAmount() - withdrawAmount;
            if (newAmount < 0) {
                //error here
                return;
            }

            accEnt.setAmount(newAmount);
            accountEntriesRepo.saveAndFlush(accEnt);
        } else { //loan or cc account
            //error here
            return;
        }
    }

    public void makeCreditCardPurchase(Long accId, Long userId, double purchaseAmount) {
        Accounts acc = getUsersOpenAccountById(accId, userId);
        if (acc == null) {
            //error here
            return;
        }

        Long accountId = acc.getAccountTypeId();
        // only credit cards can you make a purchase
        if (accountId != 4) {
            //error here
            return;
        }
        AccountEntries accEnt = accountEntriesRepo.findByAccountId(accId);
        CreditDetails credDet = creditDetailsRepo.findByAccountId(accId);

        double newAmount = accEnt.getAmount() - purchaseAmount;
        double maxLimit = credDet.getCreditLimit();
        if (newAmount < maxLimit) {
            //error here
            return;
        }
        accEnt.setAmount(newAmount);
        accountEntriesRepo.saveAndFlush(accEnt);
    }

    public void makeAccountPayment(Long accId, Long userId, double paymentAmount) {
        Accounts acc = getUsersOpenAccountById(accId, userId);
        if (acc == null) {
            //error here
            return;
        }

        Long accountId = acc.getAccountTypeId();
        //only credit cards and loans can make a payment
        if (accountId == 3 || accountId == 4) {
            AccountEntries accEnt = accountEntriesRepo.findByAccountId(accId);
            double newAmount = accEnt.getAmount() + paymentAmount;
            if (newAmount > 0) {
                //error here
                return;
            } else if (accountId == 3 && newAmount == 0) { //loan payed off close account
                acc.setOpen(false);
                accountsRepo.saveAndFlush(acc);
            }

            accEnt.setAmount(newAmount);
            accountEntriesRepo.saveAndFlush(accEnt);
        } else { //checking or savings account
            //error here
            return;
        }
    }

    public void makeTransfer(Long fromAccId, Long toAccId, Long userId, double transferAmount) {
        Accounts fromAcc = getUsersOpenAccountById(fromAccId, userId);
        if (fromAcc == null) {
            //error here
            return;
        }

        Long fromAccountType = fromAcc.getAccountTypeId();
        // cannot transfer FROM cc or loan account
        if (fromAccountType == 3 || fromAccountType == 4) {
            //error here
            return;
        }

        Accounts toAcc = getAccountByIdAndOpen(toAccId);
        if (toAcc == null) {
            //error here
            return;
        }
        Long toAccountType = toAcc.getAccountTypeId();

        //withdrawal
        AccountEntries fromAccEnt = accountEntriesRepo.findByAccountId(fromAccId);
        double fromNewAmount = fromAccEnt.getAmount() - transferAmount;
        if (fromNewAmount < 0) {
            //error here
            return;
        }

        AccountEntries toAccEnt = accountEntriesRepo.findByAccountId(toAccId);
        double toNewAmount = toAccEnt.getAmount() + transferAmount;
        if (toAccountType == 3 || toAccountType == 4) {
            if (toNewAmount > 0) {
                //error here
                return;
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
