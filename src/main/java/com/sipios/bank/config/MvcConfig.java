package com.sipios.bank.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import com.sipios.bank.model.Chat;
import com.sipios.bank.model.Role;
import com.sipios.bank.model.User;
import com.sipios.bank.repository.ChatRepository;
import com.sipios.bank.repository.RoleRepository;
import com.sipios.bank.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    @PostConstruct
    public void registerUsers() {
        List<Chat> chats = new ArrayList<>();


        Role adminRole = createRoleIfNotFound("ROLE_ADMIN");
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRoles(Arrays.asList(adminRole));
        userRepository.save(admin);

        Role userRole = createRoleIfNotFound("ROLE_USER");

        Chat chat = new Chat();
        chats.add(chat);
        chatRepository.save(chat);
        createUserIfNotFound("test", "test", Arrays.asList(userRole), Arrays.asList(chat), admin);

        Chat chat2 = new Chat();
        chats.add(chat2);
        chatRepository.save(chat2);
        createUserIfNotFound("test2", "test2", Arrays.asList(userRole), Arrays.asList(chat2), admin);

        admin.setChats(chats);
        userRepository.save(admin);
    }

    @Transactional
    private User createUserIfNotFound(String name, String password, List<Role> roles, List<Chat> chats, User advisor) {
        User user = userRepository.findByUsername(name);
        if (user == null) {
            user = new User();
            user.setUsername(name);
            user.setPassword(passwordEncoder.encode(password));
            user.setRoles(roles);
            user.setChats(chats);
            user.setAdvisor(advisor);
            userRepository.save(user);
        }

        return user;
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
