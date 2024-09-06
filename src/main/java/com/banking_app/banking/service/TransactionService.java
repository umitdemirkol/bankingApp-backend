package com.banking_app.banking.service;


import com.banking_app.banking.core.exception.GlobalException;
import com.banking_app.banking.model.dto.Transactions;
import com.banking_app.banking.model.entity.AccountEntity;
import com.banking_app.banking.model.entity.TransactionsEntity;
import com.banking_app.banking.model.entity.UsersEntity;
import com.banking_app.banking.model.enums.TransactionStatus;
import com.banking_app.banking.model.request.TransferRequest;
import com.banking_app.banking.repository.AccountRepository;
import com.banking_app.banking.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    @Async
    public CompletableFuture<String> transferMoneyAsync(TransferRequest request) throws GlobalException {
        ResponseEntity<String> responseEntity=null;
        String message=null;
            AccountEntity fromAccount = null;
            AccountEntity toAccount = null;
            try {
                fromAccount = accountRepository.findById(request.getFromAccountId()).orElseThrow(() -> new GlobalException(400L,"From account not found", HttpStatus.NOT_FOUND));
                toAccount = accountRepository.findById(request.getToAccountId()).orElseThrow(() -> new GlobalException(400L,"To account not found", HttpStatus.NOT_FOUND));
            } catch (GlobalException e) {
                throw new RuntimeException("Account or accounts not found !!"+e.getMessage());
            }

            if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
                throw new CompletionException(new GlobalException(400L, "Yeterli Bakiye Bulunamadı !!", HttpStatus.BAD_REQUEST));
            }

            fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
            toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));


            TransactionsEntity transactions = new TransactionsEntity();
            transactions.setFromAccount(fromAccount);
            transactions.setToAccount(toAccount);
            transactions.setAmount(request.getAmount());
            transactions.setTransactionDate(LocalDateTime.now());
            transactions.setStatus(Objects.requireNonNull(TransactionStatus.findByName("SUCCESS")).getValue());
            try{
                accountRepository.save(fromAccount);
                accountRepository.save(toAccount);
                transactionRepository.save(transactions);

                System.out.println("Transaction successful");
                message = String.valueOf(TransactionStatus.findByName("SUCCESS"));
                responseEntity=new ResponseEntity<>(message, HttpStatus.OK);
            }catch (Exception e){
                 message= String.valueOf(TransactionStatus.findByName("FAILED"));
                responseEntity=new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
                throw new GlobalException(400L,"Transaction Error. Exception:" + e.getMessage(),HttpStatus.BAD_REQUEST);
            }

        return CompletableFuture.completedFuture(message);
    }
//    @Transactional
//    public CompletableFuture<ResponseEntity<String>> transferMoney(TransferRequest request) throws GlobalException {
//
//        ResponseEntity<String> responseEntity=null;
//            AccountEntity fromAccount = null;
//            AccountEntity toAccount = null;
//            try {
//                fromAccount = accountRepository.findById(request.getFromAccountId()).orElseThrow(() -> new GlobalException(400L,"From account not found", HttpStatus.NOT_FOUND));
//                toAccount = accountRepository.findById(request.getToAccountId()).orElseThrow(() -> new GlobalException(400L,"To account not found", HttpStatus.NOT_FOUND));
//            } catch (GlobalException e) {
//                throw new RuntimeException("Account or accounts not found !!"+e.getMessage());
//            }
//
//            if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
//                throw new CompletionException(new GlobalException(400L, "Yeterli Bakiye Bulunamadı !!", HttpStatus.BAD_REQUEST));
//            }
//
//            fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
//            toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));
//
//
//            TransactionsEntity transactions = new TransactionsEntity();
//            transactions.setFromAccount(fromAccount);
//            transactions.setToAccount(toAccount);
//            transactions.setAmount(request.getAmount());
//            transactions.setTransactionDate(LocalDateTime.now());
//            transactions.setStatus(Objects.requireNonNull(TransactionStatus.findByName("SUCCESS")).getValue());
//            try{
//                accountRepository.save(fromAccount);
//                accountRepository.save(toAccount);
//                transactionRepository.save(transactions);
//
//                System.out.println("Transaction successful");
//                String message= String.valueOf(TransactionStatus.findByName("SUCCESS"));
//                responseEntity=new ResponseEntity<>(message, HttpStatus.OK);
//            }catch (Exception e){
//                String message= String.valueOf(TransactionStatus.findByName("FAILED"));
//                responseEntity=new ResponseEntity<>(message, HttpStatus.OK);
//                throw new GlobalException(400L,"Transaction Error. Exception:" + e.getMessage(),HttpStatus.BAD_REQUEST);
//            }
//
//        return CompletableFuture.completedFuture(responseEntity);
//    }


    @Transactional
    public ResponseEntity<String> transferMoney(TransferRequest request) throws GlobalException {

        ResponseEntity<String> responseEntity=null;
        AccountEntity fromAccount = null;
        AccountEntity toAccount = null;
        try {
            fromAccount = accountRepository.findById(request.getFromAccountId()).orElseThrow(() -> new GlobalException(400L,"From account not found", HttpStatus.NOT_FOUND));
            toAccount = accountRepository.findById(request.getToAccountId()).orElseThrow(() -> new GlobalException(400L,"To account not found", HttpStatus.NOT_FOUND));
        } catch (GlobalException e) {
            throw new RuntimeException("Account or accounts not found !!"+e.getMessage());
        }

        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new CompletionException(new GlobalException(400L, "Yeterli Bakiye Bulunamadı !!", HttpStatus.BAD_REQUEST));
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));


        TransactionsEntity transactions = new TransactionsEntity();
        transactions.setFromAccount(fromAccount);
        transactions.setToAccount(toAccount);
        transactions.setAmount(request.getAmount());
        transactions.setTransactionDate(LocalDateTime.now());
        transactions.setStatus(Objects.requireNonNull(TransactionStatus.findByName("SUCCESS")).getValue());
        try{
            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
            transactionRepository.save(transactions);

            System.out.println("Transaction successful");
            String message= String.valueOf(TransactionStatus.findByName("SUCCESS"));
            responseEntity=new ResponseEntity<>(message, HttpStatus.OK);
        }catch (Exception e){
            String message= String.valueOf(TransactionStatus.findByName("FAILED"));
            responseEntity=new ResponseEntity<>(message, HttpStatus.OK);
            throw new GlobalException(400L,"Transaction Error. Exception:" + e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    public CompletableFuture<List<Transactions>> getTransactionHistoryAsync(UUID accountId, UsersEntity currentUser) {
        return CompletableFuture.supplyAsync(() ->{
            List<Transactions> transactionsList=new ArrayList<>();
            List<TransactionsEntity> transactionsEntityList= transactionRepository.findByToAccountId(accountId);

            for(TransactionsEntity te:transactionsEntityList){
                Transactions transactions=new Transactions(te.getId(),te.getFromAccount().getId(),te.getToAccount().getId(), te.getAmount(),te.getTransactionDate(),TransactionStatus.findByValue(te.getStatus()));
                transactionsList.add(transactions);
            }


        return transactionsList;

        });
    }

    public List<Transactions> getTransactionHistory(UUID accountId, UsersEntity currentUser) {
            List<Transactions> transactionsList=new ArrayList<>();
            List<TransactionsEntity> transactionsEntityList= transactionRepository.findByToAccountId(accountId);

            for(TransactionsEntity te:transactionsEntityList){
                Transactions transactions=new Transactions(te.getId(),te.getFromAccount().getId(),te.getToAccount().getId(), te.getAmount(),te.getTransactionDate(),TransactionStatus.findByValue(te.getStatus()));
                transactionsList.add(transactions);
            }


            return transactionsList;

    }


}