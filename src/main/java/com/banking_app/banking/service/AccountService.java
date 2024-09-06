package com.banking_app.banking.service;

import com.banking_app.banking.core.exception.GlobalException;
import com.banking_app.banking.model.entity.AccountEntity;
import com.banking_app.banking.model.entity.UsersEntity;
import com.banking_app.banking.model.request.AccountRequest;
import com.banking_app.banking.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public AccountEntity createAccount(AccountRequest request, UsersEntity users) throws GlobalException {
        try {
            AccountEntity account = new AccountEntity();
            account.setUser(users);
            account.setNumber(request.getNumber());
            account.setName(request.getName());
            account.setBalance(request.getBalance());
            account.setCreatedAt(LocalDateTime.now());

            return accountRepository.save(account);
        } catch (Exception e) {
            throw new GlobalException(400L,"createAccount Error. Exception:" + e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }

    public List<AccountEntity> searchAccounts(UsersEntity currentUser) throws GlobalException {
        try {
            return accountRepository.getAccountsByUserId(currentUser.getId());
        } catch (Exception e) {
            throw new GlobalException(400L,"searchAccounts Error. Exception:" + e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    public AccountEntity getAccountDetails(UUID id,UsersEntity currentUser) throws GlobalException {
        Optional<AccountEntity> existingAccountOpt = accountRepository.findById(id);
        if (existingAccountOpt.isEmpty()) {
            throw new GlobalException(404L,"Account not found",HttpStatus.NOT_FOUND);
        }
        AccountEntity account = existingAccountOpt.get();
        if (!account.getUser().getId().equals(currentUser.getId())) {
            throw new GlobalException(401L,"Unauthorized access",HttpStatus.UNAUTHORIZED);
        }

        return account;
    }

    public AccountEntity updateAccount(UsersEntity currentUser, String number, String name, BigDecimal balance) throws GlobalException {
        Optional<AccountEntity> existingAccountOpt = accountRepository.findById(currentUser.getId());
        if (existingAccountOpt.isEmpty()) {
            throw new GlobalException(400L,"Account not found",HttpStatus.NOT_FOUND);
        }

        AccountEntity account = existingAccountOpt.get();
        if (!account.getUser().getId().equals(currentUser.getId())) {
            throw new GlobalException(401L,"Unauthorized access",HttpStatus.UNAUTHORIZED);
        }

        account.setNumber(number);
        account.setName(name);
        account.setBalance(balance);
        account.setUpdatedAt(LocalDateTime.now());

        return accountRepository.save(account);
    }

    public ResponseEntity deleteAccount(UUID id, UsersEntity currentUser) throws GlobalException {

        try {
            Optional<AccountEntity> existingAccountOpt = accountRepository.findById(id);
            if (existingAccountOpt.isEmpty()) {
                throw new GlobalException(404L,"Account not found !! ",HttpStatus.NOT_FOUND);
            }

            AccountEntity account = existingAccountOpt.get();
            if (!account.getUser().getId().equals(currentUser.getId())) {
                throw new GlobalException(401L,"Unauthorized access !! ",HttpStatus.UNAUTHORIZED);
            }

            accountRepository.deleteById(id);
        }catch (Exception e){
            throw new GlobalException(400L,"deleteAccount Error. Exception:" + e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        ResponseEntity response= new ResponseEntity(HttpStatus.OK);
        return response;
    }
}
