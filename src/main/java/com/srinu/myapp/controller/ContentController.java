package com.srinu.myapp.controller;

import com.srinu.myapp.model.User;
import com.srinu.myapp.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
public class ContentController {

    @Autowired
    private final UserRepo userRepo;

    @RequestMapping("/")
    public String dashboard() {
        return "login"; // Resolves to src/main/resources/templates/login.html
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // Resolves to src/main/resources/templates/login.html
    }

    @GetMapping("/req/signup")
    public String signup() {
        return "signup"; // Resolves to src/main/resources/templates/signup.html
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // This will return the email (used as username)

        // Fetch user details from the database
        User user = userRepo.findByUserName(username).orElse(null);
        if(user==null)
            return "redirect:/error";
        // Add user details to the model
        model.addAttribute("user", user);

        return "dashboard";
    }
    @GetMapping("/error")
    public String error() {
        return "error"; // Resolves to src/main/resources/templates/error.html
    }
}