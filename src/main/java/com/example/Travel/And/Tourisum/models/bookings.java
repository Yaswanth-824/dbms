package com.example.Travel.And.Tourisum.models;
import java.sql.Date;
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
}
