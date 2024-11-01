package com.example.Travel.And.Tourisum.service.userservice;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl.bookingiml;
import com.example.Travel.And.Tourisum.models.payment;

@Service
public class paymentservice {
    @Autowired
    bookingiml bookingiml;
    @Transactional
    public boolean bookingProgress(Long placeId, LocalDate date,payment payment) {
    try {
        // Add booking and retrieve the booking ID
        Integer bid = bookingiml.addBooking(placeId,date);
        
        if (bid != null) {  // Check if booking was successful
            // Hash the booking ID to create a payment ID
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String paymentId = passwordEncoder.encode(bid.toString());
            payment.setPaymentid(paymentId);

            return true;
        } else {
            return false;
        }
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return false; // Indicate failure
        }
    }
}
