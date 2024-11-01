package com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl;
import java.util.*;
import java.sql.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.Travel.And.Tourisum.DataAccessObject_DAO.tranport;
import com.example.Travel.And.Tourisum.models.transport;

@Repository
public class tbookingimpl implements tranport{

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
            // details.setServiceId(rs.getLong("serviceId"));
            details.setVehicleNumber(rs.getString("vehicleNumber"));
            details.setCostPerDay(rs.getFloat("costPerDay"));
            details.setOstatus(rs.getString("availabilityStatus"));
            details.setTransportTypeName(rs.getString("transportTypeName"));
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
                "tt.transportTypeName " +
                "FROM TransportOperators ta " +
                "JOIN TransportServices ts ON ta.operatorId = ts.operatorId " +
                "JOIN TransportTypes tt ON ts.transportTypeId = tt.transportTypeId " +
                "WHERE ta.placeId = ?";
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
    public void addBooking(Long placeId){
        String sql= "insert into tables transportbooking values (?,?,?,?,?) ";
        try {
            jdbcTemplate.update(sql, placeId,"root6","mode");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
