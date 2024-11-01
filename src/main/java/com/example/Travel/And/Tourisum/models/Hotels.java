package com.example.Travel.And.Tourisum.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Hotels {
    private Integer hid;
    private String Hname;
    private String Hphn;
    private String HLocation;
    private Long placeId;
}
