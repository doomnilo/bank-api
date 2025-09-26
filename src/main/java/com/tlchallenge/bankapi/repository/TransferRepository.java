package com.tlchallenge.bankapi.repository;

import com.tlchallenge.bankapi.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    List<Transfer> findByFromAccountIdOrderByTransferDateDesc(Long fromAccountId);

}

