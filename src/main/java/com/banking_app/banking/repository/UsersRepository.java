package com.banking_app.banking.repository;

import com.banking_app.banking.model.entity.UsersEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends CrudRepository<UsersEntity, String> {

    Optional<UsersEntity> findByEmail(String email);

    Optional<UsersEntity> findByUsername(String username);

}
