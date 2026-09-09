package com.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SkillGapAnalysisResponseDto {

    private Long id;

    private Long candidateId;

    private Long jobId;

    private String candidateSkills;

    private String requiredSkills;

    private String matchingSkills;

    private String missingSkills;

    private Double skillMatchPercentage;
}