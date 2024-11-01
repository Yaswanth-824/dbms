package com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl;

import java.sql.*;
import java.time.*;
import java.util.*;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.jdbc.BadSqlGrammarException;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.Travel.And.Tourisum.DataAccessObject_DAO.booking;
import com.example.Travel.And.Tourisum.models.bookings;

@Repository
public class bookingiml implements booking {
    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getName().equals("anonymousUser")) {
            return authentication.getName(); // This returns the username
        }
        return null; // If no authenticated user or user is anonymous, return null
    }

    private final JdbcTemplate jdbcTemplate;
    // Make the constructor public
    public bookingiml(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public static class bookingMapper implements RowMapper<bookings> {
        @Override
        public bookings mapRow(ResultSet rs, int rowNum) throws SQLException {
            bookings booking = new bookings();
            booking.setBid(rs.getInt("bid")); // Assuming you have a bid column
            booking.setPlaceId(rs.getLong("placeId"));
            // booking.setUsername(rs.getString("uid")); // Map the username from the database
            // booking.setStatus(rs.getString("bstatus"));
            // booking.setDay(rs.getDate("bookingdate"));
            return booking;
        }
    }
    @Override
    public List<bookings> allbookings() {
    try {
        String sql = "SELECT b.bid AS bid, "
                + "b.placeId AS placeId, "
                + "p.operatorId AS TransportOperatorID, "
                + "p.paymentid AS PaymentID, "
                + "p.mode_of_payment AS PaymentMode, "
                + "p.status AS PaymentStatus, "
                + "p.payment_date AS PaymentDate, "
                + "p.hid AS HotelID, "
                + "p.RoomID AS RoomID, "
                + "r.description AS ReviewText, "
                + "r.rating AS ReviewRating "
                + "FROM bookings b "
                + "LEFT JOIN reviews r ON r.username = b.uid AND b.placeId = r.placeId "
                + "LEFT JOIN payments p ON b.bid = p.bid "
                + "WHERE b.uid = ?";


        // Logging the query and parameters
        System.out.println("Executing query: " + sql);
        System.out.println("With parameter: " + getUserName());

        return jdbcTemplate.query(sql, new bookingMapper(), getUserName());
    } catch (BadSqlGrammarException e) {
        // Log SQL error
        System.err.println("SQL Error: " + e.getMessage());
        e.printStackTrace();
        return new ArrayList<>();
    } catch (Exception e) {
        // Log any other errors
        e.printStackTrace();
        return new ArrayList<>();
    }
}


    @Override
    public Integer addBooking(Long placeId,LocalDate date) {
        try {
            String sql = "INSERT INTO bookings (placeId, uid, bstatus, bookingdate) VALUES (?, ?, ?, ?)";
            String username = "root6";
            java.sql.Date today = java.sql.Date.valueOf(date);
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[] { "bookingId" });
                ps.setLong(1, placeId);
                ps.setString(2, username);
                ps.setString(3, "confirmed");
                ps.setDate(4, today);
                return ps;
            }, keyHolder);
            Integer generatedBookingId = keyHolder.getKey().intValue();
            System.out.println("Generated Booking ID: " + generatedBookingId);
            return generatedBookingId;

        } catch (Exception e) {
            e.printStackTrace();
            return 0; // Log the exception
        }
    }
}
