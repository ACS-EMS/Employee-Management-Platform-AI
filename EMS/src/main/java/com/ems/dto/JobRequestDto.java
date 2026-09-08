package com.ems.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JobRequestDto {

    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Employment type is required")
    private String employmentType;

    @NotNull(message = "Minimum experience is required")
    private Integer experienceMin;

    private Integer experienceMax;

    private Double salaryMin;

    private Double salaryMax;

    private String requiredSkills;

    private String preferredSkills;

    private String education;

    @NotBlank(message = "Job description is required")
    private String description;

    private String responsibilities;

    @NotNull(message = "Number of openings is required")
    private Integer openings;

    private String status;

    private LocalDateTime closingDate;
}