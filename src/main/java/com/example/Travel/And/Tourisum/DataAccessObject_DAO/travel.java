package com.example.Travel.And.Tourisum.DataAccessObject_DAO;
import java.util.*;


import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.example.Travel.And.Tourisum.models.traveldata;
// import com.example.Travel.And.Tourisum.models.travelimages;

@Repository
public interface travel {
    List<traveldata> allplaces();
    void saveimages(MultipartFile file,String id);
    List<traveldata> query(String query);
}
