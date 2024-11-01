package com.example.Travel.And.Tourisum.service.userservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Travel.And.Tourisum.DataAccessObject_DAO.impl.bookingiml;

import com.example.Travel.And.Tourisum.models.bookings;

@Service
public class bookingservice {
    @Autowired
    bookingiml bookingimp;
    public List<bookings> allbookings() {
        return bookingimp.allbookings();
    }

    
}
