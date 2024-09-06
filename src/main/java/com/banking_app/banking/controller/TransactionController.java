package com.banking_app.banking.controller;

import com.banking_app.banking.core.exception.GlobalException;
import com.banking_app.banking.model.dto.Transactions;
import com.banking_app.banking.model.entity.UsersEntity;
import com.banking_app.banking.model.request.TransferRequest;
import com.banking_app.banking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/transactions")
@EnableAsync
public class TransactionController {

    @Autowired
    TransactionService transactionService;



    // Complatabşe future ı daha önce bir projemde kullandım fakat burada işlemler yapılmasına rağmen response mesajını yönetemedim
    @PostMapping("/transferAsync")
    public CompletableFuture<ResponseEntity<String>> transferAsync(@RequestBody TransferRequest request) throws GlobalException {
        return transactionService.transferMoneyAsync(request)
                .thenApply(result -> new ResponseEntity<>(result, HttpStatus.OK));
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody TransferRequest request) throws GlobalException {

        return transactionService.transferMoney(request);
    }

    @GetMapping("/accountAsync/{accountId}")
    public CompletableFuture<List<Transactions>> viewTransactionHistoryAsync(
            @PathVariable UUID accountId, @AuthenticationPrincipal UsersEntity currentUser) {

        return transactionService.getTransactionHistoryAsync(accountId,currentUser)
                .thenApply(transactions -> transactions)
                .exceptionally(ex ->  Collections.emptyList());
    }

    @GetMapping("/account/{accountId}")
    public List<Transactions> viewTransactionHistory(
            @PathVariable UUID accountId, @AuthenticationPrincipal UsersEntity currentUser) {

        return transactionService.getTransactionHistory(accountId,currentUser);
    }

}
