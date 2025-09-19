package com.example.bankapi.controller;

import com.example.bankapi.model.Transfer;
import com.example.bankapi.model.dto.TransferDto;
import com.example.bankapi.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @GetMapping("/{id}")
    public ResponseEntity<Transfer> getTransfer(@PathVariable Long id) {
        Optional<Transfer> transfer = transferService.getTransferById(id);
        return transfer.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Transfer> createTransfer(@Valid @RequestBody TransferDto transferDto) {
        try {
            Transfer createdTransfer = transferService.createTransfer(transferDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTransfer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Transfer>> getAllTransfers() {
        List<Transfer> transfers = transferService.getAllTransfers();
        return ResponseEntity.ok(transfers);
    }
}