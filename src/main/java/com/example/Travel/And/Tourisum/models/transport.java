package com.example.Travel.And.Tourisum.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class transport {
    public Integer bid;
    public Long operatorId;
    public String operatorName;
    public String contactNumber;
    public String Ostatus;
    public Long placeId;
    public Long transportTypeId; 
    public Long serviceId;
    public float costPerDay;
    public String vehicleNumber;
    public String transportTypeName;
}
