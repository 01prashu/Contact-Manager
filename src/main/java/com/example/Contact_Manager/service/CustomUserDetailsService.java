package com.example.Contact_Manager.service;

import com.example.Contact_Manager.entities.Myuser;
import com.example.Contact_Manager.repositories.MyuserRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    MyuserRepositories myuserRepositories;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Myuser>optional_user =myuserRepositories.findByEmail(username);
        if(optional_user.isEmpty())
        {
            throw new UsernameNotFoundException("User not find enter valid username");
        }
        Myuser user = optional_user.get();
        return new CustomUserDetails(user);
    }
}
