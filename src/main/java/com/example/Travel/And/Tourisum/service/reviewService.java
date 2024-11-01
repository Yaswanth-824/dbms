package com.example.Travel.And.Tourisum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

import com.example.Travel.And.Tourisum.DataAccessObject_DAO.reviewDao;
import com.example.Travel.And.Tourisum.models.review;

@Service
public class reviewService {
    @Autowired
    reviewDao reviewDao;
    public List<review> findone(Long id) {
        return reviewDao.findbyplaceId(id);
    }



}
