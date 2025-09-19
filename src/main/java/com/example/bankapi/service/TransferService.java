package com.example.bankapi.service;

import com.example.bankapi.model.Transfer;
import com.example.bankapi.model.dto.TransferDto;

import java.util.List;
import java.util.Optional;

public interface TransferService {
    Optional<Transfer> getTransferById(Long id);
    Transfer createTransfer(TransferDto dto);
    List<Transfer> getAllTransfers();
}

