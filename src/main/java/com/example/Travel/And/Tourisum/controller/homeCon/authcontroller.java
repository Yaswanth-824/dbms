package com.example.Travel.And.Tourisum.controller.homeCon;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class authcontroller {
    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getName().equals("anonymousUser")) {
            return authentication.getName(); // This returns the username
        }
        return null; // If no authenticated user or user is anonymous, return null
    }
    @Autowired
    private AuthenticationManager authenticationManager;
    // GET mapping to display the login form
    @GetMapping("/login")
    public String showLoginPage() {
        if(getUserName()!=null){
            return "error";
        }
        return "login"; // This returns the Thymeleaf login page
    }

    // POST mapping to process login
    @PostMapping("/login")
    public String loginUser(@RequestParam("username") String username, 
                            @RequestParam("password") String password) {
        System.out.println("Step1: Received login request for user " + username);
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
            System.out.println("Step2: Authentication successful for user: " + authentication.getName());
            if (authentication.isAuthenticated()) {
                System.out.println("Step3: User " + authentication.getName() + " is authenticated.");
                return "redirect:/Home"; 
            }
        } catch (AuthenticationException e) {
            System.out.println("Step4: Authentication failed: " + e.getMessage());
            return "login"; 
        }
        return "login";
    }
}
