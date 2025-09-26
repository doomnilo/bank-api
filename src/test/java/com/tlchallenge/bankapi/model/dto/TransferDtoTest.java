package com.tlchallenge.bankapi.model.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransferDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidTransferDto() {
        TransferDto transferDto = new TransferDto();
        transferDto.setFromAccountId(1L);
        transferDto.setToAccountId(2L);
        transferDto.setAmount(BigDecimal.valueOf(100));

        Set<ConstraintViolation<TransferDto>> violations = validator.validate(transferDto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testTransferDtoWithNullFromAccountId() {
        TransferDto transferDto = new TransferDto();
        transferDto.setFromAccountId(null);
        transferDto.setToAccountId(2L);
        transferDto.setAmount(BigDecimal.valueOf(100));

        Set<ConstraintViolation<TransferDto>> violations = validator.validate(transferDto);

        assertEquals(1, violations.size());
        ConstraintViolation<TransferDto> violation = violations.iterator().next();
        assertEquals("fromAccountId", violation.getPropertyPath().toString());
    }

    @Test
    void testTransferDtoWithNullToAccountId() {
        TransferDto transferDto = new TransferDto();
        transferDto.setFromAccountId(1L);
        transferDto.setToAccountId(null);
        transferDto.setAmount(BigDecimal.valueOf(100));

        Set<ConstraintViolation<TransferDto>> violations = validator.validate(transferDto);

        assertEquals(1, violations.size());
        ConstraintViolation<TransferDto> violation = violations.iterator().next();
        assertEquals("toAccountId", violation.getPropertyPath().toString());
    }

    @Test
    void testTransferDtoWithNullAmount() {
        TransferDto transferDto = new TransferDto();
        transferDto.setFromAccountId(1L);
        transferDto.setToAccountId(2L);
        transferDto.setAmount(null);

        Set<ConstraintViolation<TransferDto>> violations = validator.validate(transferDto);

        assertEquals(1, violations.size());
        ConstraintViolation<TransferDto> violation = violations.iterator().next();
        assertEquals("amount", violation.getPropertyPath().toString());
    }

    @Test
    void testTransferDtoWithAllNullValues() {
        TransferDto transferDto = new TransferDto();
        transferDto.setFromAccountId(null);
        transferDto.setToAccountId(null);
        transferDto.setAmount(null);

        Set<ConstraintViolation<TransferDto>> violations = validator.validate(transferDto);

        assertEquals(3, violations.size());
    }

    @Test
    void testTransferDtoGettersAndSetters() {
        TransferDto transferDto = new TransferDto();

        transferDto.setFromAccountId(1L);
        transferDto.setToAccountId(2L);
        transferDto.setAmount(BigDecimal.valueOf(100));

        assertEquals(1L, transferDto.getFromAccountId());
        assertEquals(2L, transferDto.getToAccountId());
        assertEquals(BigDecimal.valueOf(100), transferDto.getAmount());
    }
}