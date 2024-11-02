package com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl;

import org.springframework.dao.DuplicateKeyException;
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
    java.sql.Date today = java.sql.Date.valueOf(roombook.getStartDate());
    try {
        String sql = "INSERT INTO Room_Bookings (RoomID, username, Hid, TotalDays, startDate, BookingStatus,bid) VALUES (?, ?, ?, ?, ?, ?,?)";
    
        String sql1 = "UPDATE Rooms SET RStatus = 'NAvail' WHERE RoomID = ?";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] { "RBID" }); // Make sure to use the correct column name
            ps.setInt(1, roombook.getRoomId());
            ps.setString(2, getUserName());
            ps.setInt(3, roombook.getHid()); 
            ps.setInt(4, roombook.getTotalDays());
            ps.setDate(5, today);
            ps.setString(6, roombook.getBookingstatus());
            ps.setInt(7,roombook.getBid());
            return ps;
        }, keyHolder);
        Long generatedRBID = keyHolder.getKey().longValue(); 
        jdbcTemplate.update(sql1, roombook.getRoomId());
        return generatedRBID;
    }catch (DuplicateKeyException e) {
        System.out.println("Duplicate key exception occurred: " + e.getMessage());
        String sql = "UPDATE Room_Bookings SET RoomID = ?, username = ?, Hid = ?, TotalDays = ?, startDate = ?, BookingStatus = ? where bid = ? ";
        jdbcTemplate.update(sql,roombook.getRoomId(),getUserName(),roombook.getHid(),1,today,"Booked",roombook.getBid());
        String sql1 = "UPDATE Rooms SET RStatus = 'NAvail' WHERE RoomID = ?";
        Long rbid = jdbcTemplate.queryForObject("SELECT RBID FROM Room_Bookings WHERE bid = ?", Long.class, roombook.getBid());
        jdbcTemplate.update(sql1,roombook.getRoomId());
        return rbid;
    } catch (Exception e) {
        System.out.println("An error occurred: " + e.getMessage());
        e.printStackTrace();
        return 0L;
    }
}


    public void availroom(Integer rid) {
        try {
            String sql1 = "UPDATE Rooms SET RStatus = 'Avail' WHERE RoomID = ?";
            jdbcTemplate.update(sql1,rid);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void navailroom(Integer rid) {
        try {
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
    public boolean addbid(Integer bid){
        try {
            String sql = "INSERT INTO Room_Bookings (bid) VALUES (?)";
            jdbcTemplate.update(sql,bid);
            return true;
        } catch (Exception e) {
            
            return false;
        }
    }
    
}
