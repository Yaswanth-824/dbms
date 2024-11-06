package com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl;

import java.sql.Date;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.example.Travel.And.Tourisum.models.cancellation;

@Repository
public class cancellationImpl {
    private final JdbcTemplate jdbcTemplate;

    // Constructor should be public for Spring to create a bean
    public cancellationImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getName().equals("anonymousUser")) {
            return authentication.getName(); // This returns the username
        }
        return null; // If no authenticated user or user is anonymous, return null
    }

    public boolean cancelbooking(Integer bid){
        try {
            String sql = "DELETE FROM TransportBookings WHERE bid = ?";
            String sql1 = "DELETE FROM Room_Bookings WHERE bid = ?";
            String sql2 = "DELETE FROM bookings WHERE bid = ?";
            jdbcTemplate.update(sql, bid);
            jdbcTemplate.update(sql1, bid);
            jdbcTemplate.update(sql2, bid);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void addCancellation(cancellation cancellation){
        Date date = Date.valueOf(cancellation.getCanceldate());
        try {
            if (cancelbooking(cancellation.getBid())) {
                String sql = "INSERT INTO cancellation(bid, cancel_date, reason, refundable_amount, username) VALUES (?, ?, ?, ?, ?)";
                jdbcTemplate.update(sql, cancellation.getBid(), date, cancellation.getReason(), cancellation.getRefundableamount(), cancellation.getUsername());
            } else {
                System.out.println("Error in Cancelling bid " + cancellation.getBid());
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            System.out.println("Error in Cancelling bid " + cancellation.getBid());
        }
    }
}
