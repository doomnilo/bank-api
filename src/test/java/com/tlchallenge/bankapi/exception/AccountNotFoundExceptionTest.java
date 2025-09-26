package com.tlchallenge.bankapi.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AccountNotFoundExceptionTest {

    @Test
    void testAccountNotFoundException() {
        Long accountId = 123L;

        AccountNotFoundException exception = new AccountNotFoundException(accountId);

        assertEquals(accountId, exception.getAccountId());
        assertEquals("Account with ID 123 not found", exception.getMessage());
    }

    @Test
    void testAccountNotFoundExceptionWithNullId() {
        Long accountId = null;

        AccountNotFoundException exception = new AccountNotFoundException(accountId);

        assertNull(exception.getAccountId());
        assertEquals("Account with ID null not found", exception.getMessage());
    }
}