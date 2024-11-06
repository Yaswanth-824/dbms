package com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl;
import java.util.*;
import java.sql.*;
import java.time.LocalDate;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.example.Travel.And.Tourisum.DataAccessObject_DAO.tranport;

import com.example.Travel.And.Tourisum.models.transport;

@Repository
public class tbookingimpl implements tranport{
    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getName().equals("anonymousUser")) {
            return authentication.getName(); // This returns the username
        }
        return null; // If no authenticated user or user is anonymous, return null
    }
    private JdbcTemplate jdbcTemplate;
    public tbookingimpl(final JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class transportmap implements RowMapper<transport> {
        @Override
        public transport mapRow(ResultSet rs, int rowNum) throws SQLException {
            transport details = new transport();
            details.setOperatorId(rs.getLong("operatorId"));
            details.setOperatorName(rs.getString("operatorName"));
            details.setContactNumber(rs.getString("contactNumber"));
            details.setOstatus(rs.getString("Ostatus"));
            details.setServiceId(rs.getLong("serviceId"));
            details.setVehicleNumber(rs.getString("vehicleNumber"));
            details.setCostPerDay(rs.getFloat("costPerDay"));
            details.setOstatus(rs.getString("availabilityStatus"));
            details.setTransportTypeName(rs.getString("transportTypeName"));
            details.setTransportTypeId(rs.getLong("transportTypeId"));
            return details;
        }
    }

    public List<transport> findTransportOperatorsByPlaceId(long placeId) {
        String sql = "SELECT " +
                "ta.operatorId, " +
                "ta.operatorName, " +
                "ta.contactNumber, " +
                "ta.Ostatus, " +
                "ts.serviceId, " +
                "ts.vehicleNumber, " +
                "ts.costPerDay, " +
                "ts.availabilityStatus, " +
                "tt.transportTypeName," +
                "ts.transportTypeId "+
                "FROM TransportOperators ta " +
                "JOIN TransportServices ts ON ta.operatorId = ts.operatorId " +
                "JOIN TransportTypes tt ON ts.transportTypeId = tt.transportTypeId " +
                "WHERE ta.placeId = ? and ta.Ostatus = 'Avail'";
        try {
            return jdbcTemplate.query(sql,new transportmap(),placeId);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
            List<transport> ans = new ArrayList<>();
            return ans;
        }
    }
    public List<transport> findbyId(Long placeId){
        String sql = "select * from transport Where placId = ?";
        try {
            return jdbcTemplate.query(sql,new transportmap(),placeId);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
            List<transport> ans = new ArrayList<>();
            return ans;
        }
    }
    public Long addBooking(transport transport) {
        
        String sql1 = "Update TransportOperators set Ostatus='NAvail' where OperatorId=?";
        String sql2 = "Update TransportOperators set Ostatus='Avail' where OperatorId=?";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        java.sql.Date today = java.sql.Date.valueOf(LocalDate.now());
        try {
            String sql = "INSERT INTO TransportBookings (username, serviceId, bookingDate, totalDays,transportTypeId, bookingStatus, OperatorId,bid) VALUES (?, ?, ?, ?, ?, ?, ?,?)";
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[] { "bookingId" });
                ps.setString(1, getUserName());
                ps.setLong(2, transport.getServiceId());
                ps.setDate(3, today);
                ps.setInt(4, 1);
                ps.setLong(5, transport.getTransportTypeId());
                ps.setString(6, "Booked");
                ps.setLong(7,transport.getOperatorId());
                ps.setInt(8,transport.getBid());
                return ps;
            }, keyHolder);
            jdbcTemplate.update(sql1,transport.getOperatorId());
            return keyHolder.getKey().longValue();
        } catch (DuplicateKeyException e) {
            String sql = "UPDATE TransportBookings SET username = ?, serviceId = ?, bookingDate = ?, totalDays = ?, transportTypeId = ?, bookingStatus = ?, OperatorId = ? WHERE bid = ?";
            jdbcTemplate.update(sql,getUserName(),transport.getServiceId(),today,1,transport.getTransportTypeId(),"Booked",transport.getOperatorId(),transport.getBid());
            Long tbid = jdbcTemplate.queryForObject("SELECT bookingId FROM TransportBookings WHERE bid = ?", Long.class, transport.getBid());
            try {
                Long OperatorId = jdbcTemplate.queryForObject("SELECT bookingId FROM TransportBookings WHERE bid = ?", Long.class, transport.getBid());
                jdbcTemplate.update(sql2,OperatorId);
            } catch (Exception e1) {
                System.out.println("Operator Not booked Previously");
            }
            jdbcTemplate.update(sql1,transport.getOperatorId());
            return tbid;
        }catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public transport findTransport(Long placeId, Long tid, Long typeId) {
        try {
            String sql = "SELECT " +
                    "ta.operatorId, " +
                    "ta.operatorName, " +
                    "ta.contactNumber, " +
                    "ta.Ostatus, " +
                    "ts.serviceId, " +
                    "ts.vehicleNumber, " +
                    "ts.costPerDay, " +
                    "ts.availabilityStatus, " +
                    "tt.transportTypeName, " +
                    "ts.transportTypeId " +
                    "FROM TransportOperators ta " +
                    "JOIN TransportServices ts ON ta.operatorId = ts.operatorId " +
                    "JOIN TransportTypes tt ON ts.transportTypeId = tt.transportTypeId " +
                    "WHERE ta.placeId = ? AND ts.operatorId = ? AND ts.transportTypeId = ?";
            
            return jdbcTemplate.queryForObject(sql, new transportmap(), placeId, tid, typeId);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
            return new transport(); // Return a new instance instead of null
        }
    }
    public boolean addbid(Integer bid){
        String sql = "INSERT INTO TransportBookings (bid) VALUES (?)";
        try {
            jdbcTemplate.update(sql, bid);
            return true;
        } catch (DuplicateKeyException e) {
            String sql1 = "Delete From TransportBookings where bid = ?";
            try {
                Long OperatorId = jdbcTemplate.queryForObject("SELECT OperatorId FROM TransportBookings WHERE bid = ?", Long.class, bid);
                String sql2 = "Update TransportOperators set Ostatus='Avail' where OperatorId=?";
                System.out.println("Operator Updated" + OperatorId);
                
                jdbcTemplate.update(sql2,OperatorId);
            } catch (Exception e1) {
                System.out.println("Operator Not booked Previously");
                return false;
            }
            jdbcTemplate.update(sql1, bid);
            jdbcTemplate.update(sql, bid);
            return true;
        }catch(Exception e1){
            System.out.println("An error occurred: " + e1.getMessage());
            return false;
        }

    }
    public float transcost(Integer bid){
        try {
            float cost = jdbcTemplate.queryForObject("Select costPerDay from TransportBookings as b ,TransportServices as s where b.bid = ? AND b.serviceId = s.serviceId",float.class,bid);
            System.out.println(cost+"transcost");
            return cost;
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        return 0.0f;
    }
}
