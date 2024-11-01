package com.example.Travel.And.Tourisum.DataAccessObject_DAO;
import java.util.*;

import org.springframework.stereotype.Repository;

import com.example.Travel.And.Tourisum.models.Hotels;

@Repository
public interface hoteldao {
    List<Hotels> findbyId(Long placeId);
    void hbook(Long placeId,Long Hid);

}
