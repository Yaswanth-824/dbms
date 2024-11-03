package com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.io.IOException;
import java.security.Principal;

import com.example.Travel.And.Tourisum.DataAccessObject_DAO.userdao;
import com.example.Travel.And.Tourisum.models.user;
import com.example.Travel.And.Tourisum.service.userservice.FileStorageService;



@Repository
public class userimpl implements userdao{
    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getName().equals("anonymousUser")) {
            return authentication.getName(); // This returns the username
        }
        return null; // If no authenticated user or user is anonymous, return null
    }
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;
    public userimpl(final JdbcTemplate jdbctemplate){
        this.jdbcTemplate = jdbctemplate;
    }
    public class UserRowMapper implements RowMapper<user> {
        @Override
        public user mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            return user.builder()
                    .userId(rs.getLong("user_id"))                     // Maps 'user_id' column to userId field
                    .username(rs.getString("username"))                 // Maps 'username' column to username field
                    .emailId(rs.getString("email_id"))                  // Maps 'email_id' column to emailId field
                    .fname(rs.getString("fname"))                        // Maps 'fname' column to fname field
                    .lname(rs.getString("lname"))                        // Maps 'lname' column to lname field
                    .gender(rs.getString("gender"))                      // Maps 'gender' column to gender field
                    .password(rs.getString("password"))                  // Maps 'password' column to password field
                    .dob(rs.getObject("dob", LocalDate.class))          // Maps 'dob' column to dob field        // Maps 'profile_photo' column to profilePhoto field
                    .phoneNumber(rs.getString("phone_number"))  
                    .profilePhoto(rs.getString("profile_photo"))        // Maps 'phone_number' column to phoneNumber field                       // Maps 'role' column to role field
                    .build();
        }
    }
    public String save(user user) {
        // SQL statement for inserting a new user
        System.out.println("Step 3");
        String sql = "INSERT INTO users (username, email_id, fname, lname, gender, password, dob, phone_number, profile_photo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String filename = "default.jpg"; // Initialize with default photo
        if (user.getProfilePhotoFile() != null && !user.getProfilePhotoFile().isEmpty()) {
            try {
                filename = FileStorageService.saveFile(user.getProfilePhotoFile(), user.getUsername());
            } catch (IOException e) {
                // Log the error and keep the default photo as the filename
                e.printStackTrace();
            }
        }
        System.out.println(filename);
        try {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
            jdbcTemplate.update(sql,
                user.getUsername(),
                user.getEmailId(),
                user.getFname(),
                user.getLname(),
                user.getGender(),
                encodedPassword,
                user.getDob(),
                user.getPhoneNumber(),
                
                filename
            );
        } catch (DataIntegrityViolationException e) {
            return "redirect:/register";
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while registering the user: ");
        }

        return "login";
    }

    @Override
    public user findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            // Attempt to query the user from the database
            return jdbcTemplate.queryForObject(sql, new UserRowMapper(), username);
        } catch (EmptyResultDataAccessException e) {
            // Handle the case when no user is found
            System.out.println("Error: No user found with username: " + getUserName());
            return null;  // You can return null or throw a custom exception here
        } catch (DataAccessException e) {
            // Handle other potential database errors
            System.out.println("Error accessing the database: " + e.getMessage());
            throw new RuntimeException("Database error occurred while finding user by username", e);
        }
        // throw new UnsupportedOperationException("Unimplemented method 'findByUsername'");
    }

    @Override
    public user findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email_id = ?";
        try {
            // Attempt to query the user from the database
            return jdbcTemplate.queryForObject(sql, new UserRowMapper(), email);
        } catch (EmptyResultDataAccessException e) {
            // Handle the case when no user is found
            System.out.println("Error: No user found with username: " + email);
            return null;  // You can return null or throw a custom exception here
        } catch (DataAccessException e) {
            // Handle other potential database errors
            System.out.println("Error accessing the database: " + e.getMessage());
            throw new RuntimeException("Database error occurred while finding user by username", e);
        }
        // throw new UnsupportedOperationException("Unimplemented method 'findByUsername'");
    }

    @Override
    public user profile(Principal principal) {
        String sql  = "select * from users where username = ?";
        return jdbcTemplate.queryForObject(sql,new UserRowMapper(),principal.getName());
    }


    public void phoneNumber(String phoneNumber){
        String sql = "update users set phone_number=? where username = ?";
        jdbcTemplate.update(sql,phoneNumber,getUserName());   
    }

    public void dob(LocalDate dob) {
        try {
            String sql = "update users set dob=? where username = ?";
        jdbcTemplate.update(sql,dob,getUserName());
        } catch (Exception e) {
            
        }
    }

    public void emailId(String emailId) {
        String sql = "update users set email_id=? where username = ?";
        jdbcTemplate.update(sql,emailId,getUserName());
    }

    public void password(String newPassword) {
        String encryptedPassword = passwordEncoder.encode(newPassword);
        System.out.println(encryptedPassword);
        String sql = "update users set password=? where username = ?";
        jdbcTemplate.update(sql,encryptedPassword,getUserName());
    }
    
    public void updatePhoto(String updatePhoto){
        String sql = "update users set profile_photo=? where username = ?";
        jdbcTemplate.update(sql,updatePhoto,getUserName());
    }
}
