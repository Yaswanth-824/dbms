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
import java.time.LocalDate;

import com.example.Travel.And.Tourisum.DataAccessObject_DAO.roomdao;
import com.example.Travel.And.Tourisum.models.RoomBookings;
import com.example.Travel.And.Tourisum.models.Rooms;

@Repository
public class roomimpl implements roomdao {
    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getName().equals("anonymousUser")) {
            return authentication.getName(); // This returns the username
        }
        return null; // If no authenticated user or user is anonymous, return null
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
    public List<Rooms> findById(Integer hid,LocalDate starDate) {
        try {
            String sql = "SELECT * From Rooms where HID = ? and RoomID Not in (SELECT RoomID From Room_Bookings where startDate=?)";


            return jdbcTemplate.query(sql, new roomMapper(), hid,starDate);
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
        try {
            Integer RoomId = jdbcTemplate.queryForObject("SELECT RoomID FROM Room_Bookings WHERE bid = ?", Integer.class, roombook.getBid());
            availroom(RoomId);
        } catch (Exception e1) {
            System.out.println("Room Not booked Previously");
        }
        String sql1 = "UPDATE Rooms SET RStatus = 'NAvail' WHERE RoomID = ?";
        Long rbid = jdbcTemplate.queryForObject("SELECT RBID FROM Room_Bookings WHERE bid = ?", Long.class, roombook.getBid());
        jdbcTemplate.update(sql1,roombook.getRoomId());
        jdbcTemplate.update(sql,roombook.getRoomId(),getUserName(),roombook.getHid(),1,today,"Booked",roombook.getBid());
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
            // e.printStackTrace();
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
        java.sql.Date today = java.sql.Date.valueOf(LocalDate.now());
        String sql = "INSERT INTO Room_Bookings (bid,startDate) VALUES (?,?)";
        try {
            jdbcTemplate.update(sql,bid,today);
            return true;
        } catch (DuplicateKeyException e) {
            System.out.println("An error occurred: " + e.getMessage());
            String sql1 = "Delete From Room_Bookings where bid = ?";
            try{
                Integer RoomId = jdbcTemplate.queryForObject("SELECT RoomID FROM Room_Bookings WHERE bid = ?", Integer.class,bid);
                availroom(RoomId);
                jdbcTemplate.update(sql1,bid);
                jdbcTemplate.update(sql,bid,today);
                System.out.println("Room booked Previously\n" +RoomId);
                return true;
            }
            catch(Exception e1){
                System.out.println("Room Not booked Previously\n");
                // jdbcTemplate.update(sql1,bid);
                jdbcTemplate.update(sql,bid,today);
                System.out.println("An error occurred: " + e1.getMessage());
                return false;
            }
        }
        catch(Exception e){
            return false;
        }
    }
    public float roomcost(Integer bid){
        try {
            float cost = jdbcTemplate.queryForObject("Select RPrice from Room_Bookings as b,Rooms as r where b.bid = ? AND r.roomID = b.roomID AND r.HID = b.Hid",float.class,bid);
            System.out.println(cost + "room");
            return cost;
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        return 0.0f;
    }
}
