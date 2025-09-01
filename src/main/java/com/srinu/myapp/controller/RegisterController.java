package com.srinu.myapp.controller;

import com.srinu.myapp.model.User;
import com.srinu.myapp.repo.UserRepo;
import com.srinu.myapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/req/signup",consumes = "application/json")
    public User createUser(@RequestBody User user){
//        System.out.println("user received"+user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }
}
