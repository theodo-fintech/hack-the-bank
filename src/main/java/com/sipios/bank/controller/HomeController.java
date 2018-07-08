package com.sipios.bank.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/")
    public String homePage(
        @RequestParam(name="debug", defaultValue="false") Boolean debug
    ) {
        if (debug)
            return "home";

        return "maintenance";
    }
}
