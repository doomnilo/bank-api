package com.tlchallenge.bankapi.controller;

import com.tlchallenge.bankapi.model.Account;
import com.tlchallenge.bankapi.model.dto.AccountDto;
import com.tlchallenge.bankapi.service.AccountService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Account>> getAccount(@PathVariable Long id) {
        logger.info("Fetching account with id={}", id);
        Optional<Account> account = accountService.getAccountById(id);

        return ResponseEntity.ok(account);
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountDto accountDto) {
        logger.info("Creating account: {}", accountDto);
        Account createdAccount = accountService.createAccount(accountDto);
        logger.info("Account created successfully with id={}", createdAccount.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @Valid @RequestBody AccountDto accountDto) {
        logger.info("Updating account with id={}: {}", id, accountDto);
        Account updatedAccount = accountService.updateAccount(id, accountDto);
        logger.info("Account updated successfully with id={}", updatedAccount.getId());

        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        logger.info("Deleting account with id={}", id);
        boolean deleted = accountService.deleteAccount(id);

        if (deleted) {
            logger.info("Account deleted successfully with id={}", id);

            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Account with id={} not found, cannot delete", id);

            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        logger.info("Fetching all accounts");
        List<Account> accounts = accountService.getAllAccounts();
        logger.debug("Number of accounts retrieved: {}", accounts.size());

        return ResponseEntity.ok(accounts);
    }
}