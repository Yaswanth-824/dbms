package com.example.Travel.And.Tourisum.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor

public class Residency {
    private Long ResidencyId;
    private Long userId;
    private Integer Hid;
}
