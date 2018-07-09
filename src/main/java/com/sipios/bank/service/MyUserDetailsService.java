package com.sipios.bank.service;

import com.sipios.bank.model.User;
import com.sipios.bank.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        System.out.println(username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            System.out.println("lol");
            throw new UsernameNotFoundException(username);
        }
        System.out.println(user.getUsername());
        return new MyUserPrincipal(user);
    }
}
