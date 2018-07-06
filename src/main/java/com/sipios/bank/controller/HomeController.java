package com.sipios.bank.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class HomeController {
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
}
