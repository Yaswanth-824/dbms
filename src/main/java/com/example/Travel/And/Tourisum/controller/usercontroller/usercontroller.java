package com.example.Travel.And.Tourisum.controller.usercontroller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Travel.And.Tourisum.models.user;
import com.example.Travel.And.Tourisum.service.userservice.userservice;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/Home")
public class usercontroller {
    @Autowired
    userservice userservice;
    @GetMapping("")
    public String getMethodName() {
        return "home";
    }
    // @GetMapping("/Booking")
    // public String booking(@RequestParam("id") Long id) {
    //     return new String();
    // }
    @GetMapping("/profile")
    public user profile(Principal principal) {
        return userservice.profile(principal);
    }

    
}
