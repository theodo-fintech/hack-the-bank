package com.sipios.bank.controller;

import com.sipios.bank.model.User;
import com.sipios.bank.repository.UserRepository;
import com.sipios.bank.service.MyUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class HomeController {
    @Autowired
    private UserRepository userRepository;

    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/")
    public String homePage(
        Model model,
        @RequestParam("maintenanceDisabled") Optional<String> maintenanceDisabled
    ) {
        if (maintenanceDisabled.isPresent()) {
            model.addAttribute("maintenanceDisabled", maintenanceDisabled);
        }
        model.addAttribute("appName", appName);
        return "home";
    }

    @GetMapping("/se-connecter")
    public String login() {
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    @GetMapping("/user-redirect")
    public String loginRedirect(Model model) {
        model.addAttribute("loginError", true);
        String username = ((MyUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username);
        return "redirect:/user/" + user.getId() + "/compte";
    }
}