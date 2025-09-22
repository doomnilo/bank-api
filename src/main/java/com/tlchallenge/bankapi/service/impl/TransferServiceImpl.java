package com.tlchallenge.bankapi.service.impl;

import com.tlchallenge.bankapi.exception.TransferRejectedException;
import com.tlchallenge.bankapi.model.Account;
import com.tlchallenge.bankapi.model.Transfer;
import com.tlchallenge.bankapi.model.dto.TransferDto;
import com.tlchallenge.bankapi.repository.AccountRepository;
import com.tlchallenge.bankapi.repository.TransferRepository;
import com.tlchallenge.bankapi.service.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransferServiceImpl implements TransferService {

    private static final Logger log = LoggerFactory.getLogger(TransferServiceImpl.class);

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private AccountRepository accountRepository;

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
        Account fromAccount = accountRepository.findById(dto.getFromAccountId()).orElseThrow(() -> new IllegalArgumentException("From account not found"));
        Account toAccount = accountRepository.findById(dto.getToAccountId()).orElseThrow(() -> new IllegalArgumentException("To account not found"));

        Transfer transfer = new Transfer();
        transfer.setFromAccountId(fromAccount.getId());
        transfer.setToAccountId(toAccount.getId());
        transfer.setAmount(dto.getAmount());
        transfer.setTransferDate(LocalDateTime.now());
        transfer.setStatus("PENDING");

        try {
            if (dto.getAmount() == null || dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                transfer.setStatus("REJECTED");
                transferRepository.save(transfer);
                throw new TransferRejectedException(
                        dto.getFromAccountId(),
                        dto.getToAccountId(),
                        "Invalid amount"
                );
            }

            if (fromAccount.getBalance().compareTo(dto.getAmount()) < 0) {
                transfer.setStatus("REJECTED");
                transferRepository.save(transfer);
                throw new TransferRejectedException(
                        dto.getFromAccountId(),
                        dto.getToAccountId(),
                        "Insufficient funds"
                );
            }

            fromAccount.setBalance(fromAccount.getBalance().subtract(dto.getAmount()));
            toAccount.setBalance(toAccount.getBalance().add(dto.getAmount()));

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

            transfer.setStatus("COMPLETED");
            return transferRepository.save(transfer);

        } catch (TransferRejectedException e) {
            log.warn("Transfer rejected: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            transfer.setStatus("PENDING");
            transferRepository.save(transfer);
            log.error("Unexpected error while executing transfer", e);
            throw new RuntimeException("Unexpected error while executing transfer", e);
        }
    }

    @Override
    public BigDecimal getAccountBalance(Long accountId) {
        return accountRepository.findById(accountId)
                .map(Account::getBalance)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    /**
     * Obtener todas las transferencias
     */
    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }
}
