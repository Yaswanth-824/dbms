package com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.sql.*;
import com.example.Travel.And.Tourisum.DataAccessObject_DAO.reviewDao;
import com.example.Travel.And.Tourisum.models.review;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Repository
public class reviewImpl implements reviewDao{
    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getName().equals("anonymousUser")) {
            return authentication.getName(); // This returns the username
        }
        return null; // If no authenticated user or user is anonymous, return null
    }
    private final JdbcTemplate jdbcTemplate;
    public reviewImpl(final JdbcTemplate jdbctemplate){
        this.jdbcTemplate = jdbctemplate;
    }

    public class reviewMapper implements RowMapper<review>{

        @Override
        public review mapRow(ResultSet rs,int rowNum) throws SQLException{
            review review = new review();
            review.setPlaceId(rs.getLong("placeId"));
            review.setUsername(rs.getString("username"));
            review.setRateing(rs.getFloat("rating"));
            review.setReview(rs.getString("description"));
            return review;

        }

    }

    @Override
    public List<review> findbyplaceId(Long Id){
        try {
            String sql = "Select * from reviews where placeId = ?";
            return jdbcTemplate.query(sql,new reviewMapper(),Id);
        } catch (Exception e) {
            List<review> ans = new ArrayList<>();
            return ans;
        }
    }

    @Override
    public void addReview(review review){
        try {
            String sql = "Insert into reviews (username,placeId,description,rating,bid) values (?,?,?,?,?)";
            jdbcTemplate.update(sql,review.getUsername(),review.getPlaceId(),review.getReview(),review.getRateing(),review.getBid());
            System.out.println("SuccesFully Inserted");
            
        } catch (DuplicateKeyException e) {
            System.out.println("Duplicate entry: " + e.getMessage());
            String sql = "Update reviews set description =?,rating=? where bid =?";
            jdbcTemplate.update(sql,review.getReview(),review.getRateing(),review.getBid());
            System.out.println("SuccesFully Updated");
        }
        catch (DataAccessException e) {
        // Handle general Spring's data access exceptions
        System.out.println("Data access error: " + e.getMessage());
        } catch (Exception e) {
            // Generic catch for any unexpected errors
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }


}
