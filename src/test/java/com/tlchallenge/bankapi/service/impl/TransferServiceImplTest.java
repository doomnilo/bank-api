package com.tlchallenge.bankapi.service.impl;

import com.tlchallenge.bankapi.exception.TransferRejectedException;
import com.tlchallenge.bankapi.model.Account;
import com.tlchallenge.bankapi.model.Transfer;
import com.tlchallenge.bankapi.model.dto.TransferDto;
import com.tlchallenge.bankapi.repository.AccountRepository;
import com.tlchallenge.bankapi.repository.TransferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransferServiceImplTest {

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransferServiceImpl transferService;

    private Transfer transfer1;
    private Transfer transfer2;
    private Account fromAccount;
    private Account toAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        transfer1 = new Transfer();
        transfer1.setId(1L);
        transfer1.setFromAccountId(1L);
        transfer1.setToAccountId(2L);
        transfer1.setAmount(BigDecimal.valueOf(100));
        transfer1.setTransferDate(LocalDateTime.now().minusHours(1));
        transfer1.setStatus("COMPLETED");

        transfer2 = new Transfer();
        transfer2.setId(2L);
        transfer2.setFromAccountId(1L);
        transfer2.setToAccountId(3L);
        transfer2.setAmount(BigDecimal.valueOf(200));
        transfer2.setTransferDate(LocalDateTime.now());
        transfer2.setStatus("COMPLETED");

        fromAccount = new Account();
        fromAccount.setId(1L);
        fromAccount.setAccountNumber("12345");
        fromAccount.setBalance(BigDecimal.valueOf(1000));

        toAccount = new Account();
        toAccount.setId(2L);
        toAccount.setAccountNumber("67890");
        toAccount.setBalance(BigDecimal.valueOf(500));
    }

    @Test
    void testGetTransferById_found() {
        when(transferRepository.findById(1L)).thenReturn(Optional.of(transfer1));

        Optional<Transfer> result = transferService.getTransferById(1L);

        assertTrue(result.isPresent());
        assertEquals(transfer1.getId(), result.get().getId());
        assertEquals(transfer1.getFromAccountId(), result.get().getFromAccountId());
        verify(transferRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTransferById_notFound() {
        when(transferRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Transfer> result = transferService.getTransferById(99L);

        assertFalse(result.isPresent());
        verify(transferRepository, times(1)).findById(99L);
    }

    @Test
    void testGetAccountBalance_found() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(1000));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        BigDecimal balance = transferService.getAccountBalance(1L);

        assertEquals(BigDecimal.valueOf(1000), balance);
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAccountBalance_notFound() {
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            transferService.getAccountBalance(99L);
        });
        verify(accountRepository, times(1)).findById(99L);
    }

    @Test
    void testGetAllTransfers() {
        List<Transfer> expectedTransfers = Arrays.asList(transfer1, transfer2);
        when(transferRepository.findAll()).thenReturn(expectedTransfers);

        List<Transfer> result = transferService.getAllTransfers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(transfer1.getId(), result.get(0).getId());
        assertEquals(transfer2.getId(), result.get(1).getId());
        verify(transferRepository, times(1)).findAll();
    }

    @Test
    void testGetTransfersByFromAccountId_success() {
        List<Transfer> expectedTransfers = Arrays.asList(transfer2, transfer1);
        when(transferRepository.findByFromAccountIdOrderByTransferDateDesc(1L)).thenReturn(expectedTransfers);

        List<Transfer> result = transferService.getTransfersByFromAccountId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(transfer2.getId(), result.get(0).getId());
        assertEquals(transfer1.getId(), result.get(1).getId());
        verify(transferRepository, times(1)).findByFromAccountIdOrderByTransferDateDesc(1L);
    }

    @Test
    void testGetTransfersByFromAccountId_emptyList() {
        when(transferRepository.findByFromAccountIdOrderByTransferDateDesc(99L)).thenReturn(Arrays.asList());

        List<Transfer> result = transferService.getTransfersByFromAccountId(99L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(transferRepository, times(1)).findByFromAccountIdOrderByTransferDateDesc(99L);
    }

    @Test
    void testCreateTransfer_success() {
        TransferDto transferDto = new TransferDto();
        transferDto.setFromAccountId(1L);
        transferDto.setToAccountId(2L);
        transferDto.setAmount(BigDecimal.valueOf(100));

        Transfer savedTransfer = new Transfer();
        savedTransfer.setId(1L);
        savedTransfer.setFromAccountId(1L);
        savedTransfer.setToAccountId(2L);
        savedTransfer.setAmount(BigDecimal.valueOf(100));
        savedTransfer.setStatus("COMPLETED");

        when(accountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(toAccount));
        when(transferRepository.save(any(Transfer.class))).thenReturn(savedTransfer);

        Transfer result = transferService.createTransfer(transferDto);

        assertNotNull(result);
        assertEquals("COMPLETED", result.getStatus());
        assertEquals(BigDecimal.valueOf(100), result.getAmount());
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(transferRepository, times(1)).save(any(Transfer.class));
    }

    @Test
    void testCreateTransfer_fromAccountNotFound() {
        TransferDto transferDto = new TransferDto();
        transferDto.setFromAccountId(99L);
        transferDto.setToAccountId(2L);
        transferDto.setAmount(BigDecimal.valueOf(100));

        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            transferService.createTransfer(transferDto);
        });

        verify(accountRepository, times(1)).findById(99L);
        verify(transferRepository, never()).save(any(Transfer.class));
    }

    @Test
    void testCreateTransfer_toAccountNotFound() {
        TransferDto transferDto = new TransferDto();
        transferDto.setFromAccountId(1L);
        transferDto.setToAccountId(99L);
        transferDto.setAmount(BigDecimal.valueOf(100));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            transferService.createTransfer(transferDto);
        });

        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).findById(99L);
        verify(transferRepository, never()).save(any(Transfer.class));
    }

    @Test
    void testCreateTransfer_invalidAmount_null() {
        TransferDto transferDto = new TransferDto();
        transferDto.setFromAccountId(1L);
        transferDto.setToAccountId(2L);
        transferDto.setAmount(null);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(toAccount));

        TransferRejectedException exception = assertThrows(TransferRejectedException.class, () -> {
            transferService.createTransfer(transferDto);
        });

        assertEquals("Invalid amount", exception.getReason());
        verify(transferRepository, times(1)).save(any(Transfer.class)); // Saves with REJECTED status
    }

    @Test
    void testCreateTransfer_invalidAmount_zero() {
        TransferDto transferDto = new TransferDto();
        transferDto.setFromAccountId(1L);
        transferDto.setToAccountId(2L);
        transferDto.setAmount(BigDecimal.ZERO);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(toAccount));

        TransferRejectedException exception = assertThrows(TransferRejectedException.class, () -> {
            transferService.createTransfer(transferDto);
        });

        assertEquals("Invalid amount", exception.getReason());
        verify(transferRepository, times(1)).save(any(Transfer.class)); // Saves with REJECTED status
    }

    @Test
    void testCreateTransfer_invalidAmount_negative() {
        TransferDto transferDto = new TransferDto();
        transferDto.setFromAccountId(1L);
        transferDto.setToAccountId(2L);
        transferDto.setAmount(BigDecimal.valueOf(-100));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(toAccount));

        TransferRejectedException exception = assertThrows(TransferRejectedException.class, () -> {
            transferService.createTransfer(transferDto);
        });

        assertEquals("Invalid amount", exception.getReason());
        verify(transferRepository, times(1)).save(any(Transfer.class)); // Saves with REJECTED status
    }

    @Test
    void testCreateTransfer_insufficientFunds() {
        TransferDto transferDto = new TransferDto();
        transferDto.setFromAccountId(1L);
        transferDto.setToAccountId(2L);
        transferDto.setAmount(BigDecimal.valueOf(2000)); // More than account balance

        when(accountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(toAccount));

        TransferRejectedException exception = assertThrows(TransferRejectedException.class, () -> {
            transferService.createTransfer(transferDto);
        });

        assertEquals("Insufficient funds", exception.getReason());
        verify(transferRepository, times(1)).save(any(Transfer.class)); // Saves with REJECTED status
        verify(accountRepository, never()).save(any(Account.class)); // Accounts not updated
    }
}