package com.ems.service;

import com.ems.common.ApiResponse;
import com.ems.common.ApplicationStatus;
import com.ems.dto.DashboardSummaryDto;
import com.ems.repository.ApplicationRepository;
import com.ems.repository.CandidateRepository;
import com.ems.repository.InterviewRepository;
import com.ems.repository.JobRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final JobRepository jobRepository;
    private final CandidateRepository candidateRepository;
    private final ApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;

    public DashboardService(
            JobRepository jobRepository,
            CandidateRepository candidateRepository,
            ApplicationRepository applicationRepository,
            InterviewRepository interviewRepository) {

        this.jobRepository = jobRepository;
        this.candidateRepository = candidateRepository;
        this.applicationRepository = applicationRepository;
        this.interviewRepository = interviewRepository;
    }

    public ResponseEntity<ApiResponse<DashboardSummaryDto>> getDashboardSummary() {

        try {

            long totalJobs = jobRepository.count();

            long activeJobs =
                    jobRepository.countByStatusIgnoreCase("ACTIVE");

            long totalCandidates =
                    candidateRepository.count();

            long totalApplications =
                    applicationRepository.count();

            long totalInterviews =
                    interviewRepository.count();

            long pendingApplications =
                    applicationRepository.countByStatus(
                            ApplicationStatus.PENDING
                    );

            DashboardSummaryDto dashboardSummary =
                    new DashboardSummaryDto(
                            totalJobs,
                            activeJobs,
                            totalCandidates,
                            totalApplications,
                            totalInterviews,
                            pendingApplications
                    );

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Dashboard summary fetched successfully",
                            dashboardSummary
                    )
            );

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Failed to fetch dashboard summary",
                                    null
                            )
                    );
        }
    }
}