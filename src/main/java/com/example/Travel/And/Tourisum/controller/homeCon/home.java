package com.example.Travel.And.Tourisum.controller.homeCon;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Travel.And.Tourisum.models.user;
import com.example.Travel.And.Tourisum.service.allPathservice;
import com.example.Travel.And.Tourisum.service.userservice.userservice;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl.travelimpl;
import com.example.Travel.And.Tourisum.models.traveldata;



@Controller
@RequestMapping("/")
public class home {
    @Autowired
    allPathservice allPathservice;
    @Autowired  
    userservice userservice;
    @Autowired
    travelimpl travelimpl;
    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getName().equals("anonymousUser")) {
            return authentication.getName(); // This returns the username
        }
        return null; // If no authenticated user or user is anonymous, return null
    }
    @GetMapping("")
    public String Homepage(@RequestParam(required = false) String query,Model model) {
        List<traveldata> ans;
        if(query == null){
            ans= travelimpl.allplaces();
        }
        else{
            ans= travelimpl.query(query);
        }

        System.out.println(getUserName());
        model.addAttribute("places", ans);
        model.addAttribute("check",getUserName());
        return "index";
    }
    @PostMapping("/register")
    public String Register(@ModelAttribute user user) {
        System.out.println("Step 1");
        return userservice.register(user);
    }
    @GetMapping("/newUser")
    public String register(Model model) {
        model.addAttribute("user", new user());  
        return "signup";
    }
    @GetMapping("/All")
    public String allplaces(Model model) {
        List<traveldata> m1 = allPathservice.allplaces();
        model.addAttribute("data", m1);
        return "paths";
    }
    
}
