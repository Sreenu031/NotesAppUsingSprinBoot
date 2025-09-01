package com.srinu.myapp.repo;

import com.srinu.myapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);

//    Optional<User> findByEmail(String );
}