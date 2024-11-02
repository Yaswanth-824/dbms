package com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.sql.*;

import com.example.Travel.And.Tourisum.DataAccessObject_DAO.roomdao;
import com.example.Travel.And.Tourisum.models.RoomBookings;
import com.example.Travel.And.Tourisum.models.Rooms;

@Repository
public class roomimpl implements roomdao {
    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName(); // This returns the username
        }
        return null; // If there's no authenticated user, return null
    }
    // Constructor should be public for Spring to create a bean
    public roomimpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private JdbcTemplate jdbcTemplate;

    // Room Mapper class
    public class roomMapper implements RowMapper<Rooms> {
        @Override
        public Rooms mapRow(ResultSet rs, int rowNum) throws SQLException {
            Rooms room = new Rooms();
            room.setHid(rs.getInt("HID"));
            room.setRprice(rs.getFloat("RPrice"));
            room.setRstatus(rs.getString("RStatus"));
            room.setRtype(rs.getString("RType"));
            room.setRoomId(rs.getInt("RoomID"));
            return room;
        }
    }

    // Query to find available rooms by hotel ID
    @Override
    public List<Rooms> findById(Integer hid) {
        try {
            String sql = "SELECT * FROM Rooms WHERE HID = ? AND Rstatus = 'Avail' ORDER BY RPrice DESC";
            return jdbcTemplate.query(sql, new roomMapper(), hid);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Method to book a room
    @Override
public Long book(RoomBookings roombook) {
    try {
        // SQL query to insert room booking details into the Room_Bookings table
        String sql = "INSERT INTO Room_Bookings (RoomID, username, Hid, TotalDays, startDate, BookingStatus,bid) VALUES (?, ?, ?, ?, ?, ?,?)";
        
        // SQL query to update room status to "Not Available"
        String sql1 = "UPDATE Rooms SET RStatus = 'NAvail' WHERE RoomID = ?";
        
        // Convert LocalDate to java.sql.Date
        java.sql.Date today = java.sql.Date.valueOf(roombook.getStartDate());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        System.out.println("check ___________"+roombook.getRoomId());
        // Perform the insertion and retrieve the generated key
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] { "RBID" }); // Make sure to use the correct column name
            ps.setInt(1, roombook.getRoomId()); // Set the RoomID
            ps.setString(2, getUserName()); // Set the username
            ps.setInt(3, roombook.getHid()); // Set the hotel ID
            ps.setInt(4, roombook.getTotalDays()); // Set the total days of booking
            ps.setDate(5, today); // Set the start date
            ps.setString(6, roombook.getBookingstatus());
            ps.setInt(7,roombook.getBid()); // Set the booking status
            return ps; // Return the prepared statement
        }, keyHolder);

        // Get the generated RBID from the inserted row
        Long generatedRBID = keyHolder.getKey().longValue(); // Extract the generated key

        // Update the room status to "Not Available"
        jdbcTemplate.update(sql1, roombook.getRoomId()); // Update room status

        return generatedRBID; // Return the new booking ID
    } catch (Exception e) {
        System.out.println("An error occurred: " + e.getMessage()); // Handle exceptions
        e.printStackTrace(); // Print stack trace for debugging
        return 0L; // Return 0 if there is an error
    }
}


    public void availroom(Integer rid) {
        try {
            // Insert room booking details into the Room_Bookings table
            
            // Update room status to "NAvail" (Not Available)
            String sql1 = "UPDATE Rooms SET RStatus = 'Avail' WHERE RoomID = ?";
            jdbcTemplate.update(sql1,rid);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void navailroom(Integer rid) {
        try {
            // Insert room booking details into the Room_Bookings table
            // Update room status to "NAvail" (Not Available)
            String sql1 = "UPDATE Rooms SET RStatus = 'Pending' WHERE RoomID = ?";
            jdbcTemplate.update(sql1,rid);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Float getcost(Integer rid){
        try {
            String sql = "select * from Rooms where RoomID = ?";
            return jdbcTemplate.queryForObject(sql,new roomMapper(),rid).getRprice();
        } catch (Exception e) {
            return 0F;
        }
    }
    public Rooms findroom(Integer hid,Integer rid){
        try {
            String sql = "SELECT * From Rooms where RoomID = ? AND HID = ?";
            Rooms room = jdbcTemplate.queryForObject(sql,new roomMapper(),rid,hid);
            return room;
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            return new Rooms();
        }
    }
    
}
