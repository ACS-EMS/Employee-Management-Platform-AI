package com.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AICandidateMatchingResponseDto {

    private Long id;

    private Long candidateId;

    private Long jobId;

    private Double skillMatch;

    private Double experienceMatch;

    private Double educationMatch;

    private Double certificationMatch;

    private Double locationMatch;

    private Double roleMatch;

    private Double overallMatchScore;

    private String explanation;
}