package com.sipios.bank.controller;

import com.sipios.bank.model.User;
import com.sipios.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AccountController {

    @Autowired
    private UserRepository userRepository;

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
        @PathVariable Long userId
    ) {
        User user = userRepository.getOne(userId);
        model.addAttribute("user", user);

        return "chat";
    }

    @GetMapping("/user/{userId}/virement")
    public String transfert(
        Model model,
        @PathVariable Long userId
    ) {
        User user = userRepository.getOne(userId);
        model.addAttribute("user", user);

        return "transfert";
    }
}
