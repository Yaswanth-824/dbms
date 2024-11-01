package com.example.Travel.And.Tourisum.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.*;

import org.springframework.web.multipart.MultipartFile;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class user {
    private Long userId;            // Primary key
    private String username;        // Unique username
    private String emailId;         // Unique email ID
    private String fname;           // First name
    private String lname;           // Last name
    private String gender;          // Gender
    private String password;        // Encrypted password
    private LocalDate dob;         
    private String phoneNumber; 
    private String role;
    private MultipartFile profilePhotoFile;
    private String profilePhoto;
}
