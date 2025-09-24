package com.tlchallenge.bankapi.service.impl;

import com.tlchallenge.bankapi.exception.AccountNotFoundException;
import com.tlchallenge.bankapi.model.Account;
import com.tlchallenge.bankapi.model.dto.AccountDto;
import com.tlchallenge.bankapi.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        account = new Account();
        account.setId(1L);
        account.setAccountNumber("12345");
        account.setBalance(BigDecimal.valueOf(1000));
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testGetAccountById_found() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Optional<Account> result = accountService.getAccountById(1L);

        assertTrue(result.isPresent());
        assertEquals("12345", result.get().getAccountNumber());
    }

    @Test
    void testGetAccountById_notFound() {
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(99L));
    }

    @Test
    void testCreateAccount_success() {
        AccountDto dto = new AccountDto("67890", BigDecimal.valueOf(500));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account created = accountService.createAccount(dto);

        assertNotNull(created);
        assertEquals("12345", created.getAccountNumber());
    }

    @Test
    void testUpdateAccount_success() {
        AccountDto dto = new AccountDto("54321", BigDecimal.valueOf(2000));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account updated = accountService.updateAccount(1L, dto);

        assertEquals("54321", updated.getAccountNumber());
        assertEquals(BigDecimal.valueOf(2000), updated.getBalance());
    }

    @Test
    void testUpdateAccount_notFound() {
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        AccountDto dto = new AccountDto("54321", BigDecimal.valueOf(2000));
        assertThrows(AccountNotFoundException.class, () -> accountService.updateAccount(99L, dto));
    }

    @Test
    void testDeleteAccount_success() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        boolean deleted = accountService.deleteAccount(1L);

        assertTrue(deleted);
        verify(accountRepository, times(1)).delete(account);
    }

    @Test
    void testDeleteAccount_notFound() {
        when(accountRepository.existsById(99L)).thenReturn(false);

        assertThrows(AccountNotFoundException.class, () -> accountService.deleteAccount(99L));
    }
}