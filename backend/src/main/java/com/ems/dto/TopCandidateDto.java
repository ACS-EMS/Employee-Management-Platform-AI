package com.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopCandidateDto {

    private Long candidateId;
    private Long jobId;
    private Double screeningScore;
    private String screeningResult;
    private String recommendation;
}