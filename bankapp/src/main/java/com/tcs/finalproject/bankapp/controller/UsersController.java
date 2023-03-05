package com.tcs.finalproject.bankapp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.finalproject.bankapp.entity.Accounts;
import com.tcs.finalproject.bankapp.entity.Users;
import com.tcs.finalproject.bankapp.service.AccountsService;
import com.tcs.finalproject.bankapp.service.UsersService;

@RestController
public class UsersController {
    private final UsersService usersService;
    private final AccountsService accountsService;

    public UsersController(UsersService usersService, AccountsService accountsService) {
        this.usersService = usersService;
        this.accountsService = accountsService;
    }

    @GetMapping("/user")
    public List<Users> getAllUsers() {
        return usersService.getAllUsers();
    }

    @GetMapping("/user/{id}")
    public Users getUserById(@PathVariable Long id) {
        return usersService.getUserById(id);
    }

    @PostMapping("/user")
    public Users saveOrUpdateUser(@RequestBody Users user) {
        return usersService.saveOrUpdateUser(user);
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable Long id) {
        usersService.deleteUser(id);
    } 

    // ------------------------------------------------------------------

    @GetMapping("/user/{id}/accounts")
    public List<Accounts> getAllUsersOpenAccounts(@PathVariable Long id) {
        return accountsService.getAllUsersOpenAccounts(id);
    }

    @GetMapping("/user/{id}/accounts/{accId}/balance")
    public double getUsersOpenAccountBalance(@PathVariable Long id, @PathVariable Long accId) {
        return accountsService.getUsersOpenAccountBalance(id, accId);
    }

    @GetMapping("/user/{id}/accounts/{accId}/purchase/{amount}")
    public void makeCreditCardPurchase(@PathVariable Long id, @PathVariable Long accId, @PathVariable double amount) {
        if (amount < 0) {
            // error here
            return;
        }

        accountsService.makeCreditCardPurchase(accId, id, amount);
    }

    @GetMapping("/user/{id}/accounts/{accId}/payment/{amount}")
    public void makeAccountPayment(@PathVariable Long id, @PathVariable Long accId, @PathVariable double amount) {
        if (amount < 0) {
            //error here
            return;
        }

        accountsService.makeAccountPayment(accId, id, amount);
    }

    @GetMapping("/user/{id}/accounts/{accId}/deposit/{amount}") 
    public void makeAccountDeposit(@PathVariable Long id, @PathVariable Long accId, @PathVariable double amount) {
        if (amount < 0) {
            //error here
            return;
        }

        accountsService.makeAccountDeposit(accId, id, amount); 
    }

    @GetMapping("/user/{id}/accounts/{accId}/withdraw/{amount}") 
    public void makeAccountWithdrawal(@PathVariable Long id, @PathVariable Long accId, @PathVariable double amount) {
        if (amount < 0) {
            //error here
            return;
        }

        accountsService.makeAccountWithdrawal(accId, id, amount);
    }

    @GetMapping("/user/{id}/accounts/{fromAccId}/transfer/{toAccId}/{amount}")
    public void makeTransfer(@PathVariable Long id, 
                             @PathVariable Long fromAccId,
                             @PathVariable Long toAccId,
                             @PathVariable double amount) {
        
        //here
        if (amount < 0) {
            //error here
            return;
        }

        accountsService.makeTransfer(fromAccId, toAccId, id, amount);

    }
}
