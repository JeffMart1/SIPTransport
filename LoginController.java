package com.example.demo.controller;

import com.example.demo.service.LoginServiceImpl;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

// LoginController.java
@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginServiceImpl());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@Valid @ModelAttribute LoginServiceImpl loginForm,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            return "login";
        }

        model.addAttribute("error", "Usuario o contraseña incorrectos");
        return "login";
    }
}
