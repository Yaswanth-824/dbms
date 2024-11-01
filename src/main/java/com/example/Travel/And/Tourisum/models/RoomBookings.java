package com.example.Travel.And.Tourisum.models;
import java.time.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor

public class RoomBookings {
    private Integer RBID;
    private Integer TotalDays;
    private Integer RoomId;
    private String Bookingstatus;
    private Integer Hid;
    private LocalDate startDate;
}
