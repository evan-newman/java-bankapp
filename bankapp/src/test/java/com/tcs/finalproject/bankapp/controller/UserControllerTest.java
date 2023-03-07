package com.tcs.finalproject.bankapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mock;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import com.tcs.finalproject.bankapp.entity.AccountEntries;
import com.tcs.finalproject.bankapp.entity.Accounts;
import com.tcs.finalproject.bankapp.service.AccountsService;
import com.tcs.finalproject.bankapp.repository.AccountsRepository;
import com.tcs.finalproject.bankapp.repository.AccountEntriesRepository;
import com.tcs.finalproject.bankapp.repository.CreditDetailsRepository;


@SpringBootTest
public class UserControllerTest {
    // @Autowired
    // private MockMvc mvc;

    private final AccountsRepository accountsRepo;

    private final AccountEntriesRepository accountEntRepo;

    private final CreditDetailsRepository credRepo;

    private final AccountsService accountService;

    // @MockBean
    // private UsersController usersController;

    @Autowired
    public UserControllerTest(AccountsRepository accountsRepo,
                              AccountEntriesRepository accountEntRepo,
                              CreditDetailsRepository credRepo,
                              AccountsService accountService) {
        this.accountsRepo = accountsRepo;
        this.accountEntRepo = accountEntRepo;
        this.credRepo = credRepo;
        this.accountService = accountService;
    }

    @Test 
    public void incorrectPurchaseAmount() throws Exception{
        Accounts acc = new Accounts();
        acc.setId(1L);
        acc.setAccountTypeId(4L);

        ResponseEntity<String> res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect amount provided");

        // given(usersController.makeCreditCardPurchase(1L, 1L, -10)).willReturn(res);

        // mvc.perform(get("/user/1/accounts/1/purchase/-10")
        //        .with(user("jadmin@example.com").password("test456"))
        //        .contentType(APPLICATION_JSON))
        //        .andExpect(status().isBadRequest());
        
    }

    @Test
    public void incorrectAccountForPurchase() throws Exception{
        Accounts acc = new Accounts();
        acc.setId(1L);
        acc.setAccountTypeId(1L);
        accountsRepo.saveAndFlush(acc);

        ResponseEntity<String> res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");

        // given(usersController.makeCreditCardPurchase(1L, 1L, 10)).willReturn(res);

        // mvc.perform(get("/user/1/accounts/1/purchase/10")
        //        .with(user("jadmin@example.com").password("test456"))
        //        .contentType(APPLICATION_JSON))
        //        .andExpect(status().isBadRequest());
    }

    @Test
    public void correctPurchase() throws Exception {
        Accounts acc = new Accounts();
        acc.setId(1L);
        acc.setAccountTypeId(4L);
        
        AccountEntries accEnt = new AccountEntries();
        accEnt.setId(1L);
        accEnt.setAccountId(1L);
        accEnt.setAmount(-500);

        this.accountsRepo.save(acc);
        accountEntRepo.saveAndFlush(accEnt);

        // ResponseEntity<String> res = ResponseEntity.status(HttpStatus.OK).body("");

        // given(usersController.makeCreditCardPurchase(1L, 1L, 100)).willReturn(res);

        accountService.makeCreditCardPurchase(1L, 1L, 100);

        // mvc.perform(get("/user/1/accounts/1/purchase/100")
        //        .with(user("jdoe@example.com").password("test123"))
        //        .contentType(APPLICATION_JSON))
        //        .andExpect(status().isOk());
        
        AccountEntries updateEnt = accountEntRepo.findById(1L).get();
        assertEquals("purhcase did not change amount", (int) updateEnt.getAmount(), -600);
    }

}
