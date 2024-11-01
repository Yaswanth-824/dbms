package com.example.Travel.And.Tourisum.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class userData {
    private Integer user_id;
    private String first_name;
    private String last_name;
    private LocalDate dob;
    private String contact_no;
    private String email_id;
}
