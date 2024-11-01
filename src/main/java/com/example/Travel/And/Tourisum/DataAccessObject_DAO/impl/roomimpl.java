package com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.sql.*;

import com.example.Travel.And.Tourisum.DataAccessObject_DAO.roomdao;
import com.example.Travel.And.Tourisum.models.RoomBookings;
import com.example.Travel.And.Tourisum.models.Rooms;

@Repository
public class roomimpl implements roomdao {
    
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
    public void book(RoomBookings roombook) {
        try {
            // Insert room booking details into the Room_Bookings table
            String sql = "INSERT INTO Room_Bookings (RoomID, username, Hid, TotalDays, startDate, BookingStatus) VALUES (?, ?, ?, ?, ?, ?)";
            
            // Update room status to "NAvail" (Not Available)
            String sql1 = "UPDATE Rooms SET RStatus = 'NAvail' WHERE RoomID = ?";
            
            // First update room status
            jdbcTemplate.update(sql1, roombook.getRoomId());
            
            // Then insert the booking
            jdbcTemplate.update(sql, roombook.getRoomId(), "someUsername");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
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
    
}
