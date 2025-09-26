package com.tlchallenge.bankapi.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TransferRejectedExceptionTest {

    @Test
    void testTransferRejectedException() {
        Long fromAccountId = 1L;
        Long toAccountId = 2L;
        String reason = "Insufficient funds";

        TransferRejectedException exception = new TransferRejectedException(fromAccountId, toAccountId, reason);

        assertEquals(fromAccountId, exception.getFromAccountId());
        assertEquals(toAccountId, exception.getToAccountId());
        assertEquals(reason, exception.getReason());

        String expectedMessage = "Transfer from account 1 to account 2 was rejected: Insufficient funds";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testTransferRejectedExceptionWithNullReason() {
        Long fromAccountId = 5L;
        Long toAccountId = 10L;
        String reason = null;

        TransferRejectedException exception = new TransferRejectedException(fromAccountId, toAccountId, reason);

        assertEquals(fromAccountId, exception.getFromAccountId());
        assertEquals(toAccountId, exception.getToAccountId());
        assertNull(exception.getReason());

        String expectedMessage = "Transfer from account 5 to account 10 was rejected: null";
        assertEquals(expectedMessage, exception.getMessage());
    }
}