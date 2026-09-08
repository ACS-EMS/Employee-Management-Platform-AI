package com.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JobResponseDto {

    private Long id;

    private String title;
    private String department;
    private String location;
    private String employmentType;

    private Integer experienceMin;
    private Integer experienceMax;

    private Double salaryMin;
    private Double salaryMax;

    private String requiredSkills;
    private String preferredSkills;

    private String education;
    private String description;
    private String responsibilities;

    private Integer openings;
    private String status;

    private LocalDateTime createdDate;
    private LocalDateTime closingDate;
}