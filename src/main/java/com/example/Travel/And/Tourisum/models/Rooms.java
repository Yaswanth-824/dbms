package com.example.Travel.And.Tourisum.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor

public class Rooms {
    private Integer RoomId;
    private Integer Hid;
    private Float Rprice;
    private String Rstatus;
    private String Rtype;
}
