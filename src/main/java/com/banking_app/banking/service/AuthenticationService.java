package com.banking_app.banking.service;

import com.banking_app.banking.model.dto.LoginUserDto;
import com.banking_app.banking.model.dto.RegisterUserDto;
import com.banking_app.banking.model.entity.UsersEntity;
import com.banking_app.banking.repository.UsersRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationService {
    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UsersRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsersEntity register(RegisterUserDto input) {
        UsersEntity user = new UsersEntity();
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setUsername(input.getEmail());
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public UsersEntity login(LoginUserDto input) {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getEmail(),
                            input.getPassword()
                    )
            );

        return userRepository.findByEmail(input.getEmail()).orElseThrow();
    }


}