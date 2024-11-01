package com.example.Travel.And.Tourisum.models;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class travelimages {
    private MultipartFile images;
    private String imgPath;
    private Long placeId;
    // private traveldata traveldata;
}
