package com.sipios.bank.config;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import com.sipios.bank.model.Role;
import com.sipios.bank.model.User;
import com.sipios.bank.repository.RoleRepository;
import com.sipios.bank.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    @PostConstruct
    public void registerUsers() {
        Role adminRole = createRoleIfNotFound("ADMIN");
        Role userRole = createRoleIfNotFound("USER");
        User test = new User();
        test.setUsername("test");
        test.setPassword(passwordEncoder.encode("test"));
        test.setRoles(Arrays.asList(userRole));

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRoles(Arrays.asList(adminRole));
        userRepository.save(admin);
    }

    @Transactional
    private Role createRoleIfNotFound(String name) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            roleRepository.save(role);
        }
        return role;
    }

}
