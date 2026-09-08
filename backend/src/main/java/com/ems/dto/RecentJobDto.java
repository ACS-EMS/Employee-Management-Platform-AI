package com.ems.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecentJobDto {

    private Long id;
    private String title;
    private String department;
    private String location;
    private String employmentType;
    private Integer openings;
    private String status;
    private LocalDateTime createdDate;
    private LocalDateTime closingDate;
}