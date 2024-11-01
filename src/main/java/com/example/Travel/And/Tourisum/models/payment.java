package com.example.Travel.And.Tourisum.models;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class payment {
    public Integer bid;
    public String username;
    public String status;
    public String mode_of_payment;
    public LocalDate payment_date;                 
    public Integer hid;
    public Integer RoomID;
    public String paymentid;
    public Long operatorId;
}
