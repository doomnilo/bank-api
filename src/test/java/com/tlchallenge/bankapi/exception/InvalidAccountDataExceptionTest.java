package com.tlchallenge.bankapi.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class InvalidAccountDataExceptionTest {

    @Test
    void testInvalidAccountDataException() {
        String field = "accountNumber";
        String message = "Account number must be unique";

        InvalidAccountDataException exception = new InvalidAccountDataException(field, message);

        assertEquals(field, exception.getField());
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testInvalidAccountDataExceptionWithNullField() {
        String field = null;
        String message = "Invalid data provided";

        InvalidAccountDataException exception = new InvalidAccountDataException(field, message);

        assertNull(exception.getField());
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testInvalidAccountDataExceptionWithNullMessage() {
        String field = "balance";
        String message = null;

        InvalidAccountDataException exception = new InvalidAccountDataException(field, message);

        assertEquals(field, exception.getField());
        assertNull(exception.getMessage());
    }
}