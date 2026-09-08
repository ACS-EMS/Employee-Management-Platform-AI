package com.ems.service;

import com.ems.common.ApiResponse;
import com.ems.common.ApplicationStatus;
import com.ems.common.InterviewStatus;
import com.ems.dto.*;
import com.ems.entity.Job;
import com.ems.mapper.DashboardMapper;
import com.ems.repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final JobRepository jobRepository;
    private final CandidateRepository candidateRepository;
    private final ApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;
    private final AIResumeScreeningRepository aiResumeScreeningRepository;
    private final DashboardMapper dashboardMapper;

    public ResponseEntity<ApiResponse<List<RecentJobDto>>>
    getRecentJobs() {

        try {

            List<RecentJobDto> recentJobs =
                    jobRepository
                            .findTop5ByOrderByCreatedDateDesc()
                            .stream()
                            .map(dashboardMapper::toRecentJobDto)
                            .toList();

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Recent jobs fetched successfully",
                            recentJobs
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Failed to fetch recent jobs",
                                    null
                            )
                    );
        }
    }
    // =========================================================
    // DASHBOARD SUMMARY
    // =========================================================

    public ResponseEntity<ApiResponse<DashboardSummaryDto>>
    getDashboardSummary() {

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

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Failed to fetch dashboard summary",
                                    null
                            )
                    );
        }
    }


    // =========================================================
    // APPLICATIONS BY STATUS
    // =========================================================

    public ResponseEntity<ApiResponse<List<ApplicationStatusCountDto>>>
    getApplicationsByStatus() {

        try {

            List<ApplicationStatusCountDto> statusCounts =
                    Arrays.stream(ApplicationStatus.values())
                            .map(status ->
                                    new ApplicationStatusCountDto(
                                            status.name(),
                                            applicationRepository
                                                    .countByStatus(status)
                                    )
                            )
                            .toList();

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Application status analytics fetched successfully",
                            statusCounts
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Failed to fetch application status analytics",
                                    null
                            )
                    );
        }
    }


    // =========================================================
    // INTERVIEWS BY STATUS
    // =========================================================

    public ResponseEntity<ApiResponse<List<InterviewStatusCountDto>>>
    getInterviewsByStatus() {

        try {

            List<InterviewStatusCountDto> statusCounts =
                    Arrays.stream(InterviewStatus.values())
                            .map(status ->
                                    new InterviewStatusCountDto(
                                            status.name(),
                                            interviewRepository
                                                    .countByStatus(status)
                                    )
                            )
                            .toList();

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Interview status analytics fetched successfully",
                            statusCounts
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Failed to fetch interview status analytics",
                                    null
                            )
                    );
        }
    }


    // =========================================================
    // APPLICATIONS PER JOB
    // =========================================================

    public ResponseEntity<ApiResponse<List<JobApplicationCountDto>>>
    getApplicationsPerJob() {

        try {

            List<Job> jobs =
                    jobRepository.findAll();

            List<JobApplicationCountDto> jobApplicationCounts =
                    jobs.stream()
                            .map(job ->
                                    new JobApplicationCountDto(
                                            job.getId(),
                                            job.getTitle(),
                                            applicationRepository
                                                    .countByJobId(
                                                            job.getId()
                                                    )
                                    )
                            )
                            .toList();

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Applications per job analytics fetched successfully",
                            jobApplicationCounts
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Failed to fetch applications per job analytics",
                                    null
                            )
                    );
        }
    }


    // =========================================================
    // RECENT APPLICATIONS
    // MapStruct used here
    // =========================================================

    public ResponseEntity<ApiResponse<List<RecentApplicationDto>>>
    getRecentApplications() {

        try {

            List<RecentApplicationDto> recentApplications =
                    applicationRepository
                            .findTop5ByOrderByAppliedAtDesc()
                            .stream()
                            .map(
                                    dashboardMapper::
                                            toRecentApplicationDto
                            )
                            .toList();

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Recent applications fetched successfully",
                            recentApplications
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Failed to fetch recent applications",
                                    null
                            )
                    );
        }
    }


    // =========================================================
    // AI SCREENING ANALYTICS
    // =========================================================

    public ResponseEntity<ApiResponse<AIScreeningAnalyticsDto>>
    getAIScreeningAnalytics() {

        try {

            long totalScreenings =
                    aiResumeScreeningRepository.count();

            Double averageScore =
                    aiResumeScreeningRepository
                            .getAverageScreeningScore();

            if (averageScore == null) {
                averageScore = 0.0;
            }

            averageScore =
                    Math.round(
                            averageScore * 100.0
                    ) / 100.0;

            long qualifiedCandidates =
                    aiResumeScreeningRepository
                            .countByScreeningResultIgnoreCase(
                                    "QUALIFIED"
                            );

            long reviewCandidates =
                    aiResumeScreeningRepository
                            .countByScreeningResultIgnoreCase(
                                    "REVIEW"
                            );

            long notQualifiedCandidates =
                    aiResumeScreeningRepository
                            .countByScreeningResultIgnoreCase(
                                    "NOT_QUALIFIED"
                            );

            AIScreeningAnalyticsDto analytics =
                    new AIScreeningAnalyticsDto(
                            totalScreenings,
                            averageScore,
                            qualifiedCandidates,
                            reviewCandidates,
                            notQualifiedCandidates
                    );

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "AI screening analytics fetched successfully",
                            analytics
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Failed to fetch AI screening analytics",
                                    null
                            )
                    );
        }
    }


    // =========================================================
    // TOP AI CANDIDATES
    // MapStruct used here
    // =========================================================

    public ResponseEntity<ApiResponse<List<TopCandidateDto>>>
    getTopAICandidates() {

        try {

            List<TopCandidateDto> topCandidates =
                    aiResumeScreeningRepository
                            .findTop5ByOrderByScreeningScoreDesc()
                            .stream()
                            .map(
                                    dashboardMapper::
                                            toTopCandidateDto
                            )
                            .toList();

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Top AI candidates fetched successfully",
                            topCandidates
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Failed to fetch top AI candidates",
                                    null
                            )
                    );
        }
    }


    // =========================================================
    // UPCOMING INTERVIEWS
    // MapStruct used here
    // =========================================================

    public ResponseEntity<ApiResponse<List<UpcomingInterviewDto>>>
    getUpcomingInterviews() {

        try {

            List<UpcomingInterviewDto> upcomingInterviews =
                    interviewRepository
                            .findTop5ByStatusAndInterviewDateTimeAfterOrderByInterviewDateTimeAsc(
                                    InterviewStatus.SCHEDULED,
                                    LocalDateTime.now()
                            )
                            .stream()
                            .map(
                                    dashboardMapper::
                                            toUpcomingInterviewDto
                            )
                            .toList();

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Upcoming interviews fetched successfully",
                            upcomingInterviews
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Failed to fetch upcoming interviews",
                                    null
                            )
                    );
        }
    }
}