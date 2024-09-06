package com.banking_app.banking.controller;

import com.banking_app.banking.core.exception.GlobalException;
import com.banking_app.banking.model.entity.AccountEntity;
import com.banking_app.banking.model.entity.UsersEntity;
import com.banking_app.banking.model.request.AccountRequest;
import com.banking_app.banking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {


    @Autowired
    private AccountService accountService;

    @PostMapping("/addAccount")
    public ResponseEntity<AccountEntity> createAccount(@RequestBody AccountRequest request, @AuthenticationPrincipal UsersEntity currentUser) throws GlobalException {
        AccountEntity createdAccount = accountService.createAccount(request,currentUser);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @GetMapping("/getAccounts")
    public ResponseEntity<List<AccountEntity>> searchAccounts(
            @AuthenticationPrincipal UsersEntity currentUser) throws GlobalException {

        List<AccountEntity> accounts = accountService.searchAccounts( currentUser);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("/getAccount/{id}")
    public ResponseEntity<AccountEntity> getAccountDetails(
            @PathVariable UUID accountId,@AuthenticationPrincipal UsersEntity currentUser) throws GlobalException {
        AccountEntity account = accountService.getAccountDetails(accountId,currentUser);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PutMapping("/updateAccount/{id}")
    public ResponseEntity<AccountEntity> updateAccount(
            @RequestParam String number,
            @RequestParam String name,
            @RequestParam BigDecimal balance,@AuthenticationPrincipal UsersEntity currentUser) throws GlobalException {

        AccountEntity updatedAccount = accountService.updateAccount(currentUser, number, name, balance);

        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseEntity> deleteAccount(
            @PathVariable UUID id,@AuthenticationPrincipal UsersEntity currentUser) throws GlobalException {

        ResponseEntity response= accountService.deleteAccount(id, currentUser);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
