package com.srinu.myapp.service;

import com.srinu.myapp.model.User;
import com.srinu.myapp.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;



    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByUserName(userName);

        if(user.isPresent()){
            var userObj = user.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(userObj.getUserName())
                    .password(userObj.getPassword())
                    .build();
        }
        else{
            throw  new UsernameNotFoundException(userName);
        }
//        throw new UnsupportedOperationException("unimplemented method:find byusername");
    }
}
