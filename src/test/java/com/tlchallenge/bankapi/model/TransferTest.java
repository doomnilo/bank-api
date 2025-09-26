package com.tlchallenge.bankapi.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TransferTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidTransfer() {
        Transfer transfer = new Transfer();
        transfer.setFromAccountId(1L);
        transfer.setToAccountId(2L);
        transfer.setAmount(BigDecimal.valueOf(100));
        transfer.setTransferDate(LocalDateTime.now());
        transfer.setStatus("PENDING");

        Set<ConstraintViolation<Transfer>> violations = validator.validate(transfer);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testTransferConstructor() {
        Long fromAccountId = 1L;
        Long toAccountId = 2L;
        BigDecimal amount = BigDecimal.valueOf(250);

        Transfer transfer = new Transfer(fromAccountId, toAccountId, amount);

        assertEquals(fromAccountId, transfer.getFromAccountId());
        assertEquals(toAccountId, transfer.getToAccountId());
        assertEquals(amount, transfer.getAmount());
        assertEquals("PENDING", transfer.getStatus());
        assertNotNull(transfer.getTransferDate());
        assertTrue(transfer.getTransferDate().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(transfer.getTransferDate().isAfter(LocalDateTime.now().minusSeconds(1)));
    }

    @Test
    void testTransferWithNullFromAccountId() {
        Transfer transfer = new Transfer();
        transfer.setFromAccountId(null);
        transfer.setToAccountId(2L);
        transfer.setAmount(BigDecimal.valueOf(100));
        transfer.setTransferDate(LocalDateTime.now());
        transfer.setStatus("PENDING");

        Set<ConstraintViolation<Transfer>> violations = validator.validate(transfer);

        assertEquals(1, violations.size());
        ConstraintViolation<Transfer> violation = violations.iterator().next();
        assertEquals("fromAccountId", violation.getPropertyPath().toString());
    }

    @Test
    void testTransferWithNullToAccountId() {
        Transfer transfer = new Transfer();
        transfer.setFromAccountId(1L);
        transfer.setToAccountId(null);
        transfer.setAmount(BigDecimal.valueOf(100));
        transfer.setTransferDate(LocalDateTime.now());
        transfer.setStatus("PENDING");

        Set<ConstraintViolation<Transfer>> violations = validator.validate(transfer);

        assertEquals(1, violations.size());
        ConstraintViolation<Transfer> violation = violations.iterator().next();
        assertEquals("toAccountId", violation.getPropertyPath().toString());
    }

    @Test
    void testTransferWithNullAmount() {
        Transfer transfer = new Transfer();
        transfer.setFromAccountId(1L);
        transfer.setToAccountId(2L);
        transfer.setAmount(null);
        transfer.setTransferDate(LocalDateTime.now());
        transfer.setStatus("PENDING");

        Set<ConstraintViolation<Transfer>> violations = validator.validate(transfer);

        assertEquals(1, violations.size());
        ConstraintViolation<Transfer> violation = violations.iterator().next();
        assertEquals("amount", violation.getPropertyPath().toString());
    }

    @Test
    void testTransferWithNullTransferDate() {
        Transfer transfer = new Transfer();
        transfer.setFromAccountId(1L);
        transfer.setToAccountId(2L);
        transfer.setAmount(BigDecimal.valueOf(100));
        transfer.setTransferDate(null);
        transfer.setStatus("PENDING");

        Set<ConstraintViolation<Transfer>> violations = validator.validate(transfer);

        assertEquals(1, violations.size());
        ConstraintViolation<Transfer> violation = violations.iterator().next();
        assertEquals("transferDate", violation.getPropertyPath().toString());
    }

    @Test
    void testTransferWithNullStatus() {
        Transfer transfer = new Transfer();
        transfer.setFromAccountId(1L);
        transfer.setToAccountId(2L);
        transfer.setAmount(BigDecimal.valueOf(100));
        transfer.setTransferDate(LocalDateTime.now());
        transfer.setStatus(null);

        Set<ConstraintViolation<Transfer>> violations = validator.validate(transfer);

        assertEquals(1, violations.size());
        ConstraintViolation<Transfer> violation = violations.iterator().next();
        assertEquals("status", violation.getPropertyPath().toString());
    }

    @Test
    void testTransferGettersAndSetters() {
        Transfer transfer = new Transfer();
        LocalDateTime now = LocalDateTime.now();

        transfer.setId(1L);
        transfer.setFromAccountId(10L);
        transfer.setToAccountId(20L);
        transfer.setAmount(BigDecimal.valueOf(500));
        transfer.setTransferDate(now);
        transfer.setStatus("COMPLETED");

        assertEquals(1L, transfer.getId());
        assertEquals(10L, transfer.getFromAccountId());
        assertEquals(20L, transfer.getToAccountId());
        assertEquals(BigDecimal.valueOf(500), transfer.getAmount());
        assertEquals(now, transfer.getTransferDate());
        assertEquals("COMPLETED", transfer.getStatus());
    }

    @Test
    void testTransferWithAllNullRequiredFields() {
        Transfer transfer = new Transfer();
        transfer.setFromAccountId(null);
        transfer.setToAccountId(null);
        transfer.setAmount(null);
        transfer.setTransferDate(null);
        transfer.setStatus(null);

        Set<ConstraintViolation<Transfer>> violations = validator.validate(transfer);

        assertEquals(5, violations.size()); // All required fields are null
    }
}