package com.example.Travel.And.Tourisum.dao;

import java.util.*;

import org.springframework.stereotype.Repository;
import com.example.Travel.And.Tourisum.models.userData;

// Crud
@Repository
public interface userdata {
    // Optional<Table>
    List<userData> findAll();
    userData findbyid(Integer id);
    String insertData(userData user);
    String changePassword(String newPassword,Integer id);
}
