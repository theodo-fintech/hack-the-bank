package com.sipios.bank.controller;

import com.sipios.bank.model.User;
import com.sipios.bank.repository.ClientRepository;
import com.sipios.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/user/{userId}/chat")
    public String chat(
        Model model,
        @PathVariable Long userId,
        @RequestParam(required = false) Long chatId
    ) {
        User user = userRepository.getOne(userId);
        if (chatId != null) {
            user.setChats(user.getChats().stream().filter(chat -> Objects.equals(chat.getId(), chatId)).collect(Collectors.toList()));
        }
        model.addAttribute("user", user);

        return "chat";
    }

    @GetMapping("/user/{userId}/clients")
    @PreAuthorize(value = "hasRole('ADMIN')")
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
