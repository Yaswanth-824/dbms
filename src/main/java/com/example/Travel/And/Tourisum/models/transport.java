package com.example.Travel.And.Tourisum.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class transport {
    public Long operatorId;
    public String operatorName;
    public String contactNumber;
    public String Ostatus;
    public Long placeId;
    public String transportTypeId; 
    public float costPerDay;
    public String vehicleNumber;
    public String transportTypeName;
}
