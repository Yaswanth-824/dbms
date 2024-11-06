package com.example.Travel.And.Tourisum.models;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class cancellation {
    public Integer bid;
    public String reason;
    public LocalDate canceldate;
    public Float refundableamount;
    public String username;
}
