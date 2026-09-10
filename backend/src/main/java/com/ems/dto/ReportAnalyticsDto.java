package com.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportAnalyticsDto {

    private long totalUsers;
    private long activeUsers;

    private long totalDepartments;

    private long totalJobs;
    private long totalApplications;
    private long totalInterviews;

    private Map<String, Long> roleDistribution;
    private Map<String, Long> departmentStatusDistribution;
    private Map<String, Long> jobsByDepartment;

    private Map<String, Long> jobStatusDistribution;
    private Map<String, Long> applicationStatusDistribution;
    private Map<String, Long> interviewStatusDistribution;

    private Map<String, Long> applicationTrends;
    private Map<String, Long> interviewTrends;
    private Map<String, Long> jobTrends;

    private double applicationToInterviewRate;
    private double selectionRate;
    private double rejectionRate;
    private double withdrawalRate;
    private double interviewCompletionRate;
    private double interviewCancellationRate;
    private double applicationsPerJob;
}