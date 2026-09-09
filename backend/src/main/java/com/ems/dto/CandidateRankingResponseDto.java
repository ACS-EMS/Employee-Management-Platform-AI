package com.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CandidateRankingResponseDto {

    private Long id;

    private Long candidateId;

    private Long jobId;

    private Double matchScore;

    private Integer rank;
}