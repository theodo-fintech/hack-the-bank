package com.sipios.bank.controller;

import com.sipios.bank.model.User;
import com.sipios.bank.repository.ClientRepository;
import com.sipios.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class AccountController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/user/{userId}/account")
    public String userHome(
        Model model,
        @PathVariable Long userId
    ) {
        User user = userRepository.getOne(userId);
        model.addAttribute("user", user);

        return "account";
    }

    @GetMapping("/user/{userId}/clients")
    public String clients(
        Model model,
        @PathVariable Long userId,
        @RequestParam String advisorId
    ) {
        User user = userRepository.getOne(userId);

        model.addAttribute("user", user);
        try {
            List<User> clients = clientRepository.getClients(advisorId);
            model.addAttribute("clients", clients);
        } catch (Exception e) {
            model.addAttribute("error");
        }

        return "clients";
    }
}
