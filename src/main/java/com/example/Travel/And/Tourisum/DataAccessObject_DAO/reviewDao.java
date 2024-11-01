package com.example.Travel.And.Tourisum.DataAccessObject_DAO;

import java.util.*;

import com.example.Travel.And.Tourisum.models.review;
public interface reviewDao {


    List<review> findbyplaceId(Long id);
    void addReview(review review);
}
