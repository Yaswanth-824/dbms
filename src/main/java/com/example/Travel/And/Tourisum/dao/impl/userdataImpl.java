package com.example.Travel.And.Tourisum.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.*;
import java.sql.*;
import com.example.Travel.And.Tourisum.dao.userdata;
import com.example.Travel.And.Tourisum.models.userData;

// import jakarta.validation.OverridesAttribute;
import lombok.NonNull;


@Repository
public class userdataImpl implements userdata{

    private final JdbcTemplate jdbcTemplate;

    //Constructor
    public userdataImpl(final JdbcTemplate jdbctemplate){
        this.jdbcTemplate = jdbctemplate;
    }

    // sql Containquestionmarks replaced by Values;
    // update --insert   query --
    public static class userRowmapper implements RowMapper<userData>{
        @Override
        public userData mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException{
            return userData.builder()
                .user_id(rs.getInt("user_id"))  // Maps 'user_id' column to userData's user_id field
                .first_name(rs.getString("first_name"))  // Maps 'first_name' column to first_name field
                .last_name(rs.getString("last_name"))  // Maps 'last_name' column to last_name field
                .dob(rs.getObject("dob", LocalDate.class))  // Maps 'dob' column to dob field
                .contact_no(rs.getString("contact_no"))  // Maps 'contact_no' column to contact_no field
                .email_id(rs.getString("email_id"))  // Maps 'email_id' column to email_id field
                .build();

        }
    }
    @Override
    public List<userData> findAll(){
        String sql = "select * from userData";
        return jdbcTemplate.query(sql,new userRowmapper());
    }
    @Override
    public userData findbyid(Integer id){
        String sql = "select * from userData where user_id = ?";
        return jdbcTemplate.queryForObject(sql,new userRowmapper(),id);
    }
    @Override
    public String insertData(userData user) {
        String sql = "INSERT INTO userData (user_id, first_name, last_name, dob, contact_no, email_id) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            user.getUser_id(),      // Corresponds to user_id
            user.getFirst_name(),   // Corresponds to first_name
            user.getLast_name(),    // Corresponds to last_name
            user.getDob(),          // Corresponds to dob
            user.getContact_no(),   // Corresponds to contact_no
            user.getEmail_id()      // Corresponds to email_id
        );
        return "Successful Insertion..........";
    }
    @Override
    public String changePassword(String newPassword,Integer id){
        //String sql = "update useData set Password = ? where user_id = ?";
        
        return "Sccesfully Updated";
    }

} 
