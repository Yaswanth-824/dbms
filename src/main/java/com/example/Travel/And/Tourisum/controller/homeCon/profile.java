package com.example.Travel.And.Tourisum.controller.homeCon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl.userimpl;
import com.example.Travel.And.Tourisum.models.user;
import com.example.Travel.And.Tourisum.service.userservice.FileStorageService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;




@Controller
@RequestMapping("/profile")
public class profile {
    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getName().equals("anonymousUser")) {
            return authentication.getName(); // This returns the username
        }
        return null; // If no authenticated user or user is anonymous, return null
    }
    @Autowired
    userimpl userimpl;
    @GetMapping("")
    public String details(Model model) {
        user user = userimpl.findByUsername(getUserName());
        model.addAttribute("detail", user);
        System.err.println(
        user.getProfilePhoto()

        );
        return "profile";
    }
    @PostMapping("/phoneNumber")
    public String phoneNumber(@RequestParam String phoneNumber) {
        userimpl.phoneNumber(phoneNumber);
        return "redirect:/profile";
    }
    @PostMapping("/dob")
    public String dob(@RequestParam LocalDate dob) {
        userimpl.dob(dob);
        return "redirect:/profile";
    }
    @PostMapping("/emailId")
    public String emailId(@RequestParam String emailId) {
        userimpl.emailId(emailId);
        return "redirect:/profile";
    }
    @PostMapping("/updatePassword")
    public String newPassword(@RequestParam String newPassword) {
        userimpl.password(newPassword);
        return "redirect:/profile";
    }
    @PostMapping("/updatePhoto")
    public String updatePhoto(@RequestParam String profilePhoto,@RequestParam MultipartFile profilePhotoFile) {
        try {
            System.out.println("check "+profilePhoto);
            
            Path oldPhotoPath = Paths.get("./src/main/resources/static/uploads/profiles/" + profilePhoto);
            Files.deleteIfExists(oldPhotoPath);
        } catch (IOException e) {
            // Redirect to the profile page or wherever needed
        }
        try {
            String newPhotoName = FileStorageService.saveFile(profilePhotoFile, getUserName());
            userimpl.updatePhoto(newPhotoName);
        } catch (Exception e) {
            // TODO: handle exception
        }
        
    
       
        // userimpl.password(newPassword);
        return "redirect:/profile";
    }
    
    
}
