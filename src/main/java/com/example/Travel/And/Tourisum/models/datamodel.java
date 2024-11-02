package com.example.Travel.And.Tourisum.models;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class datamodel {
    public Long placeId;
    public Integer bid;
    public Integer hid;
    public LocalDate startDate;
    public Integer rid;
    public Long tid;
    public Long typeId;
    public Long sId;
}
