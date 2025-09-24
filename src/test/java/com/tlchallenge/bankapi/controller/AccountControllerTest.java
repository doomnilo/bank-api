package com.tlchallenge.bankapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tlchallenge.bankapi.exception.AccountNotFoundException;
import com.tlchallenge.bankapi.model.Account;
import com.tlchallenge.bankapi.model.dto.AccountDto;
import com.tlchallenge.bankapi.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        account.setAccountNumber("12345");
        account.setBalance(BigDecimal.valueOf(1000));
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testGetAccount_found() throws Exception {
        when(accountService.getAccountById(1L)).thenReturn(Optional.of(account));

        mockMvc.perform(get("/api/v1/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("12345"));
    }

    @Test
    void testGetAccount_notFound() throws Exception {
        when(accountService.getAccountById(107L)).thenThrow(new AccountNotFoundException(107L));

        mockMvc.perform(get("/api/v1/accounts/107"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateAccount_success() throws Exception {
        AccountDto dto = new AccountDto("67890", BigDecimal.valueOf(500));
        when(accountService.createAccount(any(AccountDto.class))).thenReturn(account);

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testUpdateAccount_success() throws Exception {
        AccountDto dto = new AccountDto("54321", BigDecimal.valueOf(2000));
        when(accountService.updateAccount(Mockito.eq(1L), any(AccountDto.class))).thenReturn(account);

        mockMvc.perform(put("/api/v1/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("12345"));
    }

    @Test
    void testDeleteAccount_success() throws Exception {
        when(accountService.deleteAccount(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/accounts/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteAccount_notFound() throws Exception {
        when(accountService.deleteAccount(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/accounts/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllAccounts() throws Exception {
        when(accountService.getAllAccounts()).thenReturn(List.of(account));

        mockMvc.perform(get("/api/v1/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountNumber").value("12345"));
    }
}
