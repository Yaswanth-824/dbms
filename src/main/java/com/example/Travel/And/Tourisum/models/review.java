package com.example.Travel.And.Tourisum.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class review {
    private Long reviewId;
    private String review;
    private float rateing;
    private String username;
    private Long placeId;
    private Integer bid;
}
