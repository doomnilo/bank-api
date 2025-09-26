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

class AccountDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidAccountDto() {
        AccountDto accountDto = new AccountDto("12345", BigDecimal.valueOf(1000));

        Set<ConstraintViolation<AccountDto>> violations = validator.validate(accountDto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testAccountDtoWithNullAccountNumber() {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountNumber(null);
        accountDto.setBalance(BigDecimal.valueOf(1000));

        Set<ConstraintViolation<AccountDto>> violations = validator.validate(accountDto);

        assertEquals(1, violations.size());
        ConstraintViolation<AccountDto> violation = violations.iterator().next();
        assertEquals("accountNumber", violation.getPropertyPath().toString());
        assertEquals("Account number is required", violation.getMessage());
    }

    @Test
    void testAccountDtoWithBlankAccountNumber() {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountNumber("");
        accountDto.setBalance(BigDecimal.valueOf(1000));

        Set<ConstraintViolation<AccountDto>> violations = validator.validate(accountDto);

        assertEquals(1, violations.size());
        ConstraintViolation<AccountDto> violation = violations.iterator().next();
        assertEquals("accountNumber", violation.getPropertyPath().toString());
        assertEquals("Account number is required", violation.getMessage());
    }

    @Test
    void testAccountDtoWithWhitespaceOnlyAccountNumber() {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountNumber("   ");
        accountDto.setBalance(BigDecimal.valueOf(1000));

        Set<ConstraintViolation<AccountDto>> violations = validator.validate(accountDto);

        assertEquals(1, violations.size());
        ConstraintViolation<AccountDto> violation = violations.iterator().next();
        assertEquals("accountNumber", violation.getPropertyPath().toString());
        assertEquals("Account number is required", violation.getMessage());
    }

    @Test
    void testAccountDtoWithNullBalance() {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountNumber("12345");
        accountDto.setBalance(null);

        Set<ConstraintViolation<AccountDto>> violations = validator.validate(accountDto);

        assertEquals(1, violations.size());
        ConstraintViolation<AccountDto> violation = violations.iterator().next();
        assertEquals("balance", violation.getPropertyPath().toString());
        assertEquals("Balance is required", violation.getMessage());
    }

    @Test
    void testAccountDtoWithAllInvalidValues() {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountNumber("");
        accountDto.setBalance(null);

        Set<ConstraintViolation<AccountDto>> violations = validator.validate(accountDto);

        assertEquals(2, violations.size());
    }

    @Test
    void testAccountDtoConstructorAndGetters() {
        String accountNumber = "67890";
        BigDecimal balance = BigDecimal.valueOf(2500);

        AccountDto accountDto = new AccountDto(accountNumber, balance);

        assertEquals(accountNumber, accountDto.getAccountNumber());
        assertEquals(balance, accountDto.getBalance());
    }

    @Test
    void testAccountDtoSettersAndGetters() {
        AccountDto accountDto = new AccountDto();

        accountDto.setAccountNumber("98765");
        accountDto.setBalance(BigDecimal.valueOf(500));

        assertEquals("98765", accountDto.getAccountNumber());
        assertEquals(BigDecimal.valueOf(500), accountDto.getBalance());
    }

    @Test
    void testAccountDtoWithZeroBalance() {
        AccountDto accountDto = new AccountDto("12345", BigDecimal.ZERO);

        Set<ConstraintViolation<AccountDto>> violations = validator.validate(accountDto);

        assertTrue(violations.isEmpty()); // Zero balance should be valid
    }

    @Test
    void testAccountDtoWithNegativeBalance() {
        AccountDto accountDto = new AccountDto("12345", BigDecimal.valueOf(-100));

        Set<ConstraintViolation<AccountDto>> violations = validator.validate(accountDto);

        assertTrue(violations.isEmpty()); // Negative balance should be valid at DTO level
    }
}