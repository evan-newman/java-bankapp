package com.tcs.finalproject.bankapp.controller;

import java.util.List;
import java.util.ArrayList;

import org.apache.tomcat.util.http.parser.MediaType;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcs.finalproject.bankapp.entity.Accounts;
import com.tcs.finalproject.bankapp.entity.CreditDetails;
import com.tcs.finalproject.bankapp.repository.AccountsRepository;
import com.tcs.finalproject.bankapp.repository.CreditDetailsRepository;
import com.tcs.finalproject.bankapp.service.AccountsService;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

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

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@WebMvcTest(AdminController.class)
// @AutoConfigureTestDatabase(replace = NONE)
public class AdminControllerTest {
    @Autowired
    private MockMvc mvc;

    @Mock
    private AccountsRepository accountsRepo;

    @Mock
    private CreditDetailsRepository credRepo;

    @MockBean
    private AdminController adminController;

    @Test
    public void getAllAccounts() throws Exception {
        Accounts acc = new Accounts();
        acc.setName("checking");

        List<Accounts> allAccounts = new ArrayList<Accounts>();
        allAccounts.add(acc);
        ResponseEntity<List<Accounts>> res = ResponseEntity.status(HttpStatus.OK).body(allAccounts);

        given(adminController.getAllAccounts()).willReturn(res);

        mvc.perform(get("/admin/accounts")
               .with(user("jadmin@example.com").password("test456"))
               .contentType(APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$[0].name", is(acc.getName())));
    }

    @Test
    public void closeAccount() throws Exception {
        Accounts acc = new Accounts();
        acc.setOpen(true);
        acc.setId(1L);
        accountsRepo.saveAndFlush(acc);

        acc.setOpen(false);
        ResponseEntity<Accounts> res = ResponseEntity.status(HttpStatus.OK).body(acc);
        given(adminController.closeAnAccount(1L)).willReturn(res);

        mvc.perform(get("/admin/accounts/1/close")
               .with(user("jadmin@example.com").password("test456"))
               .contentType(APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("open", is(false)));
    }

    @Test
    public void badUpdateCreditRequest() throws Exception {
        Accounts acc = new Accounts();
        acc.setOpen(true);
        acc.setId(1L);
        CreditDetails cred = new CreditDetails();
        cred.setId(1L);
        cred.setAccountId(1L);
        cred.setCreditLimit(-10000);
        
        accountsRepo.saveAndFlush(acc);
        credRepo.saveAndFlush(cred);
        cred.setCreditLimit(-12000);


        ResponseEntity<CreditDetails> res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        String credAsJson = new ObjectMapper().writeValueAsString(cred); 
        given(adminController.updateCreditAccountDetails(cred)).willReturn(res);

        MvcResult mvcRes = mvc.perform(post("/admin/accounts/update/credit")
               .with(user("jadmin@example.com").password("test456"))
               .accept(APPLICATION_JSON)
               .contentType(APPLICATION_JSON)
               .content(credAsJson)).andReturn();

        System.out.println(mvcRes.getResponse().getStatus());
        System.out.println(HttpStatus.BAD_REQUEST.value());

        assertEquals("Result should be bad!", mvcRes.getResponse().getStatus(), HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void goodUpdateCreditRequest() throws Exception {
        Accounts acc = new Accounts();
        acc.setOpen(true);
        acc.setId(1L);
        CreditDetails cred = new CreditDetails();
        cred.setId(1L);
        cred.setAccountId(1L);
        cred.setCreditLimit(-10000);
        cred.setCcv(773);
        
        accountsRepo.saveAndFlush(acc);
        credRepo.saveAndFlush(cred);
        cred.setCcv(882);


        ResponseEntity<CreditDetails> res = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        given(adminController.updateCreditAccountDetails(cred)).willReturn(res);

        mvc.perform(get("/admin/accounts/update/credit")
               .with(user("jadmin@example.com").password("test456"))
               .contentType(APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("ccv", is(cred.getCcv())));
    }
}
