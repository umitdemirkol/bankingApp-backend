package com.banking_app.banking.repository;

import com.banking_app.banking.model.entity.TransactionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionsEntity, Long> {
    List<TransactionsEntity> findByToAccountId(UUID toAccountId);
}