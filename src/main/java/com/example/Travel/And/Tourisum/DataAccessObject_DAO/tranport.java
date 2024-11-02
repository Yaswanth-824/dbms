package com.example.Travel.And.Tourisum.DataAccessObject_DAO;
import java.util.*;
import org.springframework.stereotype.Repository;

import com.example.Travel.And.Tourisum.models.transport;

@Repository
public interface tranport {
    List<transport> findbyId(Long placId);
    Long addBooking(transport transport);
}
