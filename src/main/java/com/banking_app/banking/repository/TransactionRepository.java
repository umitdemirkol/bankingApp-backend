package com.banking_app.banking.repository;

import com.banking_app.banking.model.entity.TransactionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionsEntity, Long> {
    List<TransactionsEntity> findByFromAccountId(UUID toAccountId);
    @Query( nativeQuery = true,value = "SELECT * FROM Transactions WHERE FROM_ACCOUNT_ID IN :fromAccountIds")
    List<TransactionsEntity> findByAccountIds(@Param("fromAccountIds") List<UUID> fromAccountIds);
}