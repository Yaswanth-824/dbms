package com.example.Travel.And.Tourisum.DataAccessObject_DAO;
import java.time.LocalDate;
import java.util.*;

import com.example.Travel.And.Tourisum.models.RoomBookings;
import com.example.Travel.And.Tourisum.models.Rooms;
import org.springframework.stereotype.Repository;

@Repository
public interface roomdao {
    List<Rooms> findById(Integer Hid,LocalDate starDate);
    Long book(RoomBookings roombook);
}
