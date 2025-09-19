package com.example.bankapi.service.impl;

import com.example.bankapi.model.Account;
import com.example.bankapi.model.dto.AccountDto;
import com.example.bankapi.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl {

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Obtener cuenta por ID
     */
    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    /**
     * Crear nueva cuenta a partir de AccountDto
     */
    public Account createAccount(AccountDto dto) {
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
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) {
            throw new RuntimeException("Account not found");
        }

        Account account = optionalAccount.get();
        account.setAccountNumber(dto.getAccountNumber());
        account.setBalance(dto.getBalance());
        account.setUpdatedAt(LocalDateTime.now());

        return accountRepository.save(account);
    }

    /**
     * Eliminar cuenta
     */
    public boolean deleteAccount(Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) {
            return false;
        }
        accountRepository.deleteById(id);
        return true;
    }

    /**
     * Obtener todas las cuentas
     */
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}

