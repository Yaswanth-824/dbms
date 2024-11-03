package com.example.Travel.And.Tourisum.controller.homeCon;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
// ---------Meathods of Mapping 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

// import org.springframework.web.bind.annotation.Controller;
import org.springframework.web.multipart.MultipartFile;

import com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl.travelimpl;
import com.example.Travel.And.Tourisum.models.review;
import com.example.Travel.And.Tourisum.models.traveldata;
// import com.example.Travel.And.Tourisum.models.travelimages;
// import com.example.Travel.And.Tourisum.DataAccessObject_DAO.travel;
import com.example.Travel.And.Tourisum.models.userData;
import com.example.Travel.And.Tourisum.service.allPathservice;
import com.example.Travel.And.Tourisum.service.reviewService;

//---------To Get Data Grom Urls
//import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.PathVariable;





@Controller
@RequestMapping("/place")    // Allows All Types of Mapping i.e GetMapping(Display Data),PostMapping(Get Data From User),PutMapping(Updatting Data),DeleteMapping(Deleteing Data),Etc..

public class allPathController {
    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getName().equals("anonymousUser")) {
            return authentication.getName(); // This returns the username
        }
        return null; // If no authenticated user or user is anonymous, return null
    }
    @Autowired
    private allPathservice allPathservice;
    @Autowired
    private reviewService reviewService;
    @Autowired
    travelimpl travelimpl;
    @GetMapping("")
    public String allplaces(Model model) {
        List<traveldata> m1 = allPathservice.allplaces();
        model.addAttribute("data", m1);
        model.addAttribute("check",getUserName());
        return "paths";
    }
    @GetMapping("{placeId}")
    public String place(@PathVariable Long placeId,Model model) {
        traveldata ans =  travelimpl.findbyId(placeId);
        model.addAttribute("data",ans);
        return "explore";
    }
    
    @GetMapping("/Review/{id}")
    public String review(@PathVariable("id") Long id,Model model) {
        List<review> ans = reviewService.findone(id);
        model.addAttribute("data", ans);
        return "review";

    }
    @GetMapping("/review")
    public userData all(@RequestParam("para") Integer id) {
        return allPathservice.findone(id);
    }
    @PostMapping("/save")
    public String postMethodName(@RequestParam MultipartFile file,@RequestParam String placeid) {

        allPathservice.saveimages(file,placeid);
        return "redirect:/";
    }
}




















// @GetMapping("/{from_to}")
    // public List<allPathmodel> Desired(@PathVariable Integer from_to){     @PathVariable is used get Value From Path
    //     return allPathservice1.Desired(from_to);
    // }
    /*  http://localhost:8000/sum?a=10;
        
        @GetMapping("/sum")
        public Integer getMethodName(@RequestParam("a") Integer a) {
            return a*10;
        }
     * 
     * 
     */
    // public List<Map<Long, traveldata>> allplaces(Model model) {
    //     List<Map<Long, traveldata>> m1 = allPathservice.allplaces();
    //     model.addAttribute("data", m1);
    //     // System.out.println(m1.travel);
    //     // return "paths"
    //     return m1;
    // }
    // public List<userData> AlltravelRoutes(){
    //     return allPathservice.AlltravelRoutes();
    // }