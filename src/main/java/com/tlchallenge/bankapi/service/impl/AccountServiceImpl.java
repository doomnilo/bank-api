package com.tlchallenge.bankapi.service.impl;

import com.tlchallenge.bankapi.exception.AccountNotFoundException;
import com.tlchallenge.bankapi.exception.InvalidAccountDataException;
import com.tlchallenge.bankapi.model.Account;
import com.tlchallenge.bankapi.model.dto.AccountDto;
import com.tlchallenge.bankapi.repository.AccountRepository;
import com.tlchallenge.bankapi.service.AccountService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Obtener cuenta por ID
     */
    @CircuitBreaker(name = "account-service", fallbackMethod = "fallbackGetAccount")
    public Optional<Account> getAccountById(Long id) {
        return Optional.ofNullable(accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id)));
    }

    /**
     * Fallback method for getAccountById
     */
    public Optional<Account> fallbackGetAccount(Long id, Exception ex) {
        // Log the fallback and return empty optional
        return Optional.empty();
    }

    /**
     * Crear nueva cuenta a partir de AccountDto
     */
    public Account createAccount(AccountDto dto) {
        if (dto.getAccountNumber() == null || dto.getAccountNumber().isBlank()) {
            throw new InvalidAccountDataException("account number", "Account number cannot be null or empty");
        }
        if (dto.getBalance() == null || dto.getBalance().signum() < 0) {
            throw new InvalidAccountDataException("account balance", "Balance must be non-negative");
        }

        Account account = new Account();
        account.setAccountNumber(dto.getAccountNumber());
        account.setBalance(dto.getBalance());
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        return accountRepository.save(account);
    }

    /**
     * Actualizar cuenta existente
     */
    public Account updateAccount(Long id, AccountDto dto) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));

        if (dto.getAccountNumber() == null || dto.getAccountNumber().isBlank()) {
            throw new InvalidAccountDataException("account number", "Account number cannot be null or empty");
        }
        if (dto.getBalance() == null || dto.getBalance().signum() < 0) {
            throw new InvalidAccountDataException("account balance", "Balance must be non-negative");
        }

        account.setAccountNumber(dto.getAccountNumber());
        account.setBalance(dto.getBalance());
        account.setUpdatedAt(LocalDateTime.now());

        return accountRepository.save(account);
    }

    /**
     * Eliminar cuenta
     */
    public boolean deleteAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        accountRepository.delete(account);

        return true;
    }

    /**
     * Obtener todas las cuentas
     */
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}

