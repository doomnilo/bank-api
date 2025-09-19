package com.example.bankapi.service;

import com.example.bankapi.model.Account;
import com.example.bankapi.model.dto.AccountDto;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Optional<Account> getAccountById(Long id);
    Account createAccount(AccountDto dto);
    Account updateAccount(Long id, AccountDto dto);
    boolean deleteAccount(Long id);
    List<Account> getAllAccounts();
}
