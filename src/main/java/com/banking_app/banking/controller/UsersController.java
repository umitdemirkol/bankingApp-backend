package com.banking_app.banking.controller;

import com.banking_app.banking.core.exception.GlobalException;
import com.banking_app.banking.model.entity.UsersEntity;
import com.banking_app.banking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UsersController {

    @GetMapping(value = "/me")
    public ResponseEntity<UsersEntity> authenticatedUser() throws GlobalException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UsersEntity currentUser = (UsersEntity) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }

}
