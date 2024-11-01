package com.example.Travel.And.Tourisum.controller.usercontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;

import com.example.Travel.And.Tourisum.service.userservice.tripService;


@Controller
@RequestMapping("/Home/Trips")
public class tripController {
    @Autowired
    tripService tripService;
    @GetMapping("/")
    public String viewAllTrips() {
        return tripService.viewAllTrips();
    }
    

}
