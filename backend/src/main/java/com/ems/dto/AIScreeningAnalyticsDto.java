package com.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AIScreeningAnalyticsDto {

    private Long totalScreenings;

    private Double averageScreeningScore;

    private Long qualifiedCandidates;

    private Long reviewCandidates;

    private Long notQualifiedCandidates;
}