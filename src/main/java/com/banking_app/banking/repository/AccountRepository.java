package com.banking_app.banking.repository;

import com.banking_app.banking.model.entity.AccountEntity;
import com.banking_app.banking.model.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {


    @Query(nativeQuery = true,value = "SELECT * FROM Account WHERE USER_ID = :userId")
    List<AccountEntity> getAccountsByUserId(
            @Param("userId") UUID userId
    );
}
