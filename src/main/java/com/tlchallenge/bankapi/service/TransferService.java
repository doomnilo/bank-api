package com.tlchallenge.bankapi.service;

import com.tlchallenge.bankapi.model.Transfer;
import com.tlchallenge.bankapi.model.dto.TransferDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TransferService {
    Optional<Transfer> getTransferById(Long id);
    Transfer createTransfer(TransferDto dto);
    List<Transfer> getAllTransfers();
    BigDecimal getAccountBalance(Long accountId);
    List<Transfer> getTransfersByFromAccountId(Long fromAccountId);
}

