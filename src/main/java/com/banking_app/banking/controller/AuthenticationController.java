package com.banking_app.banking.controller;

import com.banking_app.banking.core.exception.GlobalException;
import com.banking_app.banking.model.dto.LoginUserDto;
import com.banking_app.banking.model.dto.RegisterUserDto;
import com.banking_app.banking.model.entity.UsersEntity;
import com.banking_app.banking.model.response.LoginResponse;
import com.banking_app.banking.service.AuthenticationService;
import com.banking_app.banking.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    @Autowired
    private  JwtService jwtService;
    @Autowired
    private  AuthenticationService authenticationService;


    @PostMapping("/register")
    public ResponseEntity<UsersEntity> register(@RequestBody RegisterUserDto registerUserDto) throws GlobalException {

        try{
            UsersEntity registeredUser = authenticationService.register(registerUserDto);

            return ResponseEntity.ok(registeredUser);
        }catch (Exception e){
            throw  new GlobalException(401L,"Register user Exception. Exception message : "+e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) throws GlobalException {
        try {
            UsersEntity authenticatedUser = authenticationService.login(loginUserDto);

            String jwtToken = jwtService.generateToken(authenticatedUser);

            LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());
            return ResponseEntity.ok(loginResponse);
        }catch (Exception e){
            throw  new GlobalException(401L,"Login user Exception. Exception Message : "+e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

    }

    @GetMapping("/test")
    public ResponseEntity<String> authenticate() {
        String test="test";
        return ResponseEntity.ok(test);
    }
}
