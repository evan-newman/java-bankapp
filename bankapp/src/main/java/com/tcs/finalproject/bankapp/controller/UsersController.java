package com.tcs.finalproject.bankapp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.tcs.finalproject.bankapp.exception.BankException;

@RestController
public class UsersController {
    private final UsersService usersService;
    private final AccountsService accountsService;

    public UsersController(UsersService usersService, AccountsService accountsService) {
        this.usersService = usersService;
        this.accountsService = accountsService;
    }

    @GetMapping("/admin/user")
    public List<Users> getAllUsers() {
        return usersService.getAllUsers();
    }

    @GetMapping("/admin/user/{id}")
    public Users getUserById(@PathVariable Long id) {
        return usersService.getUserById(id);
    }

    @PostMapping("/admin/user/new")
    public Users saveOrUpdateUser(@RequestBody Users user) {
        return usersService.saveOrUpdateUser(user);
    }

    @DeleteMapping("/admin/user/remove/{id}")
    public void deleteUser(@PathVariable Long id) {
        usersService.deleteUser(id);
    } 

    // ------------------------------------------------------------------

    @GetMapping("/user/{id}/accounts")
    public ResponseEntity<List<Accounts>> getAllUsersOpenAccounts(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(accountsService.getAllUsersOpenAccounts(id));
    }

    @GetMapping("/user/{id}/accounts/{accId}/balance")
    public ResponseEntity<Double> getUsersOpenAccountBalance(@PathVariable Long id, @PathVariable Long accId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(accountsService.getUsersOpenAccountBalance(id, accId));
        } catch (BankException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/user/{id}/accounts/{accId}/purchase/{amount}")
    public ResponseEntity<String> makeCreditCardPurchase(@PathVariable Long id, @PathVariable Long accId, @PathVariable double amount) {
        if (amount < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect amount provided");
        }

        try {
            accountsService.makeCreditCardPurchase(accId, id, amount);
            return ResponseEntity.status(HttpStatus.OK).body("account updated successfully");
        } catch (BankException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/user/{id}/accounts/{accId}/payment/{amount}")
    public ResponseEntity<String> makeAccountPayment(@PathVariable Long id, @PathVariable Long accId, @PathVariable double amount) {
        if (amount < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect amount provided");
        }

        try {
            accountsService.makeAccountPayment(accId, id, amount); // FIX HERE
            return ResponseEntity.status(HttpStatus.OK).body("account updated successfully");
        } catch (BankException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/user/{id}/accounts/{accId}/deposit/{amount}") 
    public ResponseEntity<String> makeAccountDeposit(@PathVariable Long id, @PathVariable Long accId, @PathVariable double amount) {
        if (amount < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect amount provided");
        }

        try {
            accountsService.makeAccountDeposit(accId, id, amount); 
            return ResponseEntity.status(HttpStatus.OK).body("account updated successfully");
        } catch (BankException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/user/{id}/accounts/{accId}/withdraw/{amount}") 
    public ResponseEntity<String> makeAccountWithdrawal(@PathVariable Long id, @PathVariable Long accId, @PathVariable double amount) {
        if (amount < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect amount provided");
        }

        try {
            accountsService.makeAccountWithdrawal(accId, id, amount);
            return ResponseEntity.status(HttpStatus.OK).body("account updated successfully");
        } catch (BankException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/user/{id}/accounts/{fromAccId}/transfer/{toAccId}/{amount}")
    public ResponseEntity<String> makeTransfer(@PathVariable Long id, 
                             @PathVariable Long fromAccId,
                             @PathVariable Long toAccId,
                             @PathVariable double amount) {
        
        if (amount < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect amount provided");
        }

        try {
            accountsService.makeTransfer(fromAccId, toAccId, id, amount);
            return ResponseEntity.status(HttpStatus.OK).body("transfer between accounts done successfully");
        } catch (BankException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
