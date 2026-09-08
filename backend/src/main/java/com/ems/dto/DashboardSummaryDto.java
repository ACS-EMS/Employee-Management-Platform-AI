package com.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDto {

    private Long totalJobs;
    private Long activeJobs;
    private Long totalCandidates;
    private Long totalApplications;
    private Long totalInterviews;
    private Long pendingApplications;
}