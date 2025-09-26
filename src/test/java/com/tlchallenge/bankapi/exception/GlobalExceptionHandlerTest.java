package com.tlchallenge.bankapi.exception;

import com.tlchallenge.bankapi.model.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Test error message");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleIllegalArgumentException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test error message", response.getBody().getMessage());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().getStatus());
    }

    @Test
    void testHandleValidationException() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError1 = new FieldError("transferDto", "amount", "must not be null");
        FieldError fieldError2 = new FieldError("transferDto", "fromAccountId", "must not be null");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(fieldError1, fieldError2));

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getMessage().contains("amount: must not be null"));
        assertTrue(response.getBody().getMessage().contains("fromAccountId: must not be null"));
    }

    @Test
    void testHandleGenericException() {
        Exception exception = new RuntimeException("Unexpected error");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Unexpected error occurred", response.getBody().getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getBody().getStatus());
    }

    @Test
    void testHandleTransferRejected() {
        TransferRejectedException exception = new TransferRejectedException(1L, 2L, "Insufficient funds");

        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleTransferRejected(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> body = response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.get("status"));
        assertEquals("Transfer Rejected", body.get("error"));
        assertEquals("Insufficient funds", body.get("message"));
        assertEquals(1L, body.get("fromAccountId"));
        assertEquals(2L, body.get("toAccountId"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    void testHandleAccountNotFound() {
        AccountNotFoundException exception = new AccountNotFoundException(123L);

        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleAccountNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> body = response.getBody();
        assertEquals(HttpStatus.NOT_FOUND.value(), body.get("status"));
        assertEquals("Account Not Found", body.get("error"));
        assertEquals(123L, body.get("accountId"));
        assertNotNull(body.get("timestamp"));
        assertNotNull(body.get("message"));
    }

    @Test
    void testHandleInvalidAccountData() {
        InvalidAccountDataException exception = new InvalidAccountDataException("accountNumber", "Account number must be unique");

        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleInvalidAccountData(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> body = response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.get("status"));
        assertEquals("Invalid Account Data", body.get("error"));
        assertEquals("accountNumber", body.get("field"));
        assertEquals("Account number must be unique", body.get("message"));
        assertNotNull(body.get("timestamp"));
    }
}