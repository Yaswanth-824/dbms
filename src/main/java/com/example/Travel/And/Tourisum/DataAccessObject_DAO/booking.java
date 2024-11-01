package com.example.Travel.And.Tourisum.DataAccessObject_DAO;

import org.springframework.stereotype.Repository;

import com.example.Travel.And.Tourisum.models.bookings;

import java.time.LocalDate;
import java.util.*;
@Repository
public interface booking {
    List<bookings> allbookings();
    Integer addBooking(Long placeId,LocalDate date);
}
