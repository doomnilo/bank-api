package com.tlchallenge.bankapi.service.impl;

import com.tlchallenge.bankapi.model.Transfer;
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

import static org.junit.jupiter.api.Assertions.*;
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
    }

    @Test
    void getTransferById() {
    }

    @Test
    void createTransfer() {
    }

    @Test
    void getAccountBalance() {
    }

    @Test
    void getAllTransfers() {
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
}