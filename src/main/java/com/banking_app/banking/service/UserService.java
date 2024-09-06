package com.banking_app.banking.service;

import com.banking_app.banking.core.exception.GlobalException;
import com.banking_app.banking.model.entity.UsersEntity;
import com.banking_app.banking.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {

    @Autowired
    UsersRepository usersRepository;


    public UserService(UsersRepository userRepository) {
        this.usersRepository = userRepository;
    }

    public List<User> allUsers()  throws GlobalException {
        try {
            List<User> users = new ArrayList<>();

            Iterable<UsersEntity> users1=usersRepository.findAll();
            for (UsersEntity user : users1) {
                User a1=new User(user.getUsername(),user.getPassword(),new ArrayList<>());
                users.add(a1);
            }

            return users;
        }catch (Exception e){
            throw  new GlobalException(400L,"allUsers Error. Exception:" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
