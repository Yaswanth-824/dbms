package com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.Travel.And.Tourisum.DataAccessObject_DAO.hoteldao;
import com.example.Travel.And.Tourisum.models.Hotels;

@Repository
public class hotelimpl implements hoteldao {

    private JdbcTemplate jdbcTemplate;
    public hotelimpl(final JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public class hotelMapper implements RowMapper<Hotels>{
        @Override
        public Hotels mapRow(ResultSet rs,int rowNum) throws SQLException{
            Hotels hotel = new Hotels();
            hotel.setHid(rs.getInt("HID"));
            hotel.setHname(rs.getString("HName"));
            hotel.setHphn(rs.getString("HPhone"));
            hotel.setHLocation(rs.getString("HLocation"));
            hotel.setPlaceId(rs.getLong("placeId"));
            return hotel;
        }
    }

    @Override
    public List<Hotels> findbyId(Long placeId){
        try {
            String sql = "select * from Hotels where placeId = ?";
            return jdbcTemplate.query(sql,new hotelMapper(),placeId);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace(); 
            List<Hotels> ans = new ArrayList<>();
            return ans;      
        }
    }
    @Override
    public void hbook(Long placeId,Long hid){
        try {
            String sql = "insert into Hotels values (?,?,?,?)";
            jdbcTemplate.update(sql, placeId,hid);
            
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();         }
    }

}
