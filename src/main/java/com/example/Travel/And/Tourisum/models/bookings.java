package com.example.Travel.And.Tourisum.models;
import java.sql.Date;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class bookings {
    private Integer bid;
    private Long placeId;
    private String username;
    private String status;
    private Date day;
    private LocalDate date;
    private Integer hid;
    private Integer rid;
    private Long transportTypeId;
    private Long OperatorId;
    private Long serviceId;
}
