package com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

// import com.example.Travel.And.Tourisum.Config.fileconfig;
import com.example.Travel.And.Tourisum.DataAccessObject_DAO.travel;
// import com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl.travelimpl.TravelMapper;
import com.example.Travel.And.Tourisum.models.traveldata;
import com.example.Travel.And.Tourisum.models.travelimages;
import com.example.Travel.And.Tourisum.service.userservice.FileStorageService;

// import jakarta.validation.OverridesAttribute;

@Repository
public class travelimpl implements travel{
    private JdbcTemplate jdbcTemplate;
    public travelimpl(final JdbcTemplate jdbctemplate){
        this.jdbcTemplate = jdbctemplate;
    }


    public class TravelMapper implements RowMapper<traveldata> {

    @Override
    public traveldata mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Create traveldata object for the current row
        traveldata travelData = new traveldata();
        travelData.setPlaceId(rs.getLong("placeId")); // Fetch placeId
        travelData.setTitle(rs.getString("title"));
        travelData.setDescription(rs.getString("description"));
        travelData.setTotalCost(rs.getLong("cost"));
        travelData.setDays(rs.getInt("noOfDays"));

        // Create travelimages object
        travelimages travelImage = new travelimages();
        travelImage.setImgPath(rs.getString("image")); // Fetch image path
        travelImage.setPlaceId(rs.getLong("placeId"));
        // Add image to the traveldata object
        if (travelData.getImages() == null) {
            travelData.setImages(new ArrayList<>());
        }
        travelData.getImages().add(travelImage);

        return travelData;
    }

    }
    
    public class imageMapper implements RowMapper<travelimages>{
        @Override
        public travelimages mapRow(ResultSet rs,int rowNum) throws SQLException{
            travelimages travelimages = new travelimages();
            travelimages.setImgPath(rs.getString("image"));
            travelimages.setPlaceId(rs.getLong("placeId"));
            return travelimages;
        }
    }

    @Override
    public List<traveldata> allplaces() {
        try {
            String sql = "SELECT * FROM travelPlaces LEFT JOIN placeImages ON placeImages.placeId = travelPlaces.placeId";
            List<traveldata> travelDataList = jdbcTemplate.query(sql, new TravelMapper());

            // Create a new list to store unique traveldata entries
            List<traveldata> uniqueTravelDataList = new ArrayList<>();

            for (traveldata travelData : travelDataList) {
                // Check if the travelData with this placeId is already in the unique list
                boolean exists = false;
                for (traveldata uniqueTravelData : uniqueTravelDataList) {
                    if (uniqueTravelData.getPlaceId().equals(travelData.getPlaceId())) {
                        exists = true;
                        // If it exists, add images to the existing traveldata
                        uniqueTravelData.getImages().addAll(travelData.getImages());
                        break;
                    }
                }
                // If it doesn't exist, add it to the unique list
                if (!exists) {
                    uniqueTravelDataList.add(travelData);
                }
            }

            return uniqueTravelDataList;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while registering the user: ");
        }
        // return null;
    }
    //---------for saving images 
    @Override
    public void saveimages(MultipartFile image,String placeid){
        
        try {
            String sql = "Insert into placeImages values(?,?)";
            System.out.println("Step 1");
            String filename = FileStorageService.savePlaceFile(image,placeid);
            System.out.println(filename);
            System.out.println("Step 3");
            Long pid = Long.valueOf(placeid);
            jdbcTemplate.update(sql,pid,filename);
        } catch (DuplicateKeyException e) {
            // Handle duplicate entry case
            System.out.println("Duplicate entry: " + e.getMessage());
           }  // Your logic to save the image and associate it with the place ID
        // }
        catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage(), e);
        }
    

    }

    public List<traveldata> query(String query) {
        try {
            String sql = "SELECT * FROM travelPlaces LEFT JOIN placeImages ON placeImages.placeId = travelPlaces.placeId where travelPlaces.title like ?";
            String like  = query+"%";
            List<traveldata> travelDataList = jdbcTemplate.query(sql, new TravelMapper(),like);

            // Create a new list to store unique traveldata entries
            List<traveldata> uniqueTravelDataList = new ArrayList<>();

            for (traveldata travelData : travelDataList) {
                // Check if the travelData with this placeId is already in the unique list
                boolean exists = false;
                for (traveldata uniqueTravelData : uniqueTravelDataList) {
                    if (uniqueTravelData.getPlaceId().equals(travelData.getPlaceId())) {
                        exists = true;
                        // If it exists, add images to the existing traveldata
                        uniqueTravelData.getImages().addAll(travelData.getImages());
                        break;
                    }
                }
                // If it doesn't exist, add it to the unique list
                if (!exists) {
                    uniqueTravelDataList.add(travelData);
                }
            }

            return uniqueTravelDataList;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while registering the user: ");
        }
        // return null;
    }
    public traveldata findbyId(Long placeId){
        try {
            String sql = "SELECT * FROM travelPlaces LEFT JOIN placeImages ON placeImages.placeId = travelPlaces.placeId where travelPlaces.placeId=?";
            List<traveldata> ans = jdbcTemplate.query(sql,new TravelMapper(),placeId);
            traveldata fans = new traveldata();
            fans.setPlaceId(placeId);
            if (!ans.isEmpty()) {
                fans.setImages(new ArrayList<>()); // Initialize the image list
                for (traveldata result : ans) {
                    if (result.getPlaceId().equals(placeId)) { // Check for matching placeId
                        if (result.getImages() != null) {
                            fans.setDescription(result.getDescription());
                            fans.setDays(result.getDays());
                            fans.setTitle(result.getTitle());
                            fans.setTotalCost(result.getTotalCost());
                            fans.getImages().addAll(result.getImages());
                        }
                    }
                }
            }
            return fans;
        } catch (Exception e) {
            return new traveldata();
        }
    }

    //--------- for saving places 
    // @Override

}
