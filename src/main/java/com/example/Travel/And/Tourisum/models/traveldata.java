package com.example.Travel.And.Tourisum.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class traveldata {
    private Long placeId;
    private String title;
    private String description;
    private Long totalCost;
    private Integer Days;
    private List<travelimages> images;
}
