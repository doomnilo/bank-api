package com.example.bankapi.service.impl;

import com.example.bankapi.model.Transfer;
import com.example.bankapi.model.dto.TransferDto;
import com.example.bankapi.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransferServiceImpl {

    @Autowired
    private TransferRepository transferRepository;

    /**
     * Obtener transferencia por ID
     */
    public Optional<Transfer> getTransferById(Long id) {
        return transferRepository.findById(id);
    }

    /**
     * Crear nueva transferencia
     */
    public Transfer createTransfer(TransferDto dto) {
        Transfer transfer = new Transfer();
        transfer.setFromAccountId(dto.getFromAccountId());
        transfer.setToAccountId(dto.getToAccountId());
        transfer.setAmount(dto.getAmount());
        transfer.setTransferDate(LocalDateTime.now());
        transfer.setStatus(dto.getStatus() != null ? dto.getStatus() : "PENDING");

        return transferRepository.save(transfer);
    }

    /**
     * Obtener todas las transferencias
     */
    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }
}
