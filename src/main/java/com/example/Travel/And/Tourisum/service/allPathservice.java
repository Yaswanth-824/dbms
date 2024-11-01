package com.example.Travel.And.Tourisum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import com.example.Travel.And.Tourisum.DataAccessObject_DAO.travel;
import com.example.Travel.And.Tourisum.DataAccessObject_DAO.userdao;
import com.example.Travel.And.Tourisum.dao.userdata;
import com.example.Travel.And.Tourisum.models.traveldata;
// import com.example.Travel.And.Tourisum.models.travelimages;
import com.example.Travel.And.Tourisum.models.user;
// import com.example.Travel.And.Tourisum.dao.impl.*;
import com.example.Travel.And.Tourisum.models.userData;

@Service
public class allPathservice {

    @Autowired
    userdata userdata;
    @Autowired
    userdao userdao;
    @Autowired
    travel travel;
    public List<userData> AlltravelRoutes(){
        return userdata.findAll();
    }
    public userData findone(Integer id){
        return userdata.findbyid(id);
    }
    public String insertData(userData user) {
        return userdata.insertData(user);
    }
    public String Register(user user) {
        return userdao.save(user);
    }
    public void saveimages(MultipartFile file, String placeid) {
        travel.saveimages(file,placeid);
    }
    public List<traveldata> allplaces() {
        return travel.allplaces();
    }

}
