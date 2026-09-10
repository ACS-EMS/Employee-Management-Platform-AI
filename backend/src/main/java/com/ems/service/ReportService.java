package com.ems.service;

import com.ems.common.ApiResponse;
import com.ems.dto.ReportAnalyticsDto;
import com.ems.mapper.ReportMapper;
import com.ems.repository.ApplicationRepository;
import com.ems.repository.DepartmentRepository;
import com.ems.repository.InterviewRepository;
import com.ems.repository.JobRepository;
import com.ems.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReportService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;
    private final ReportMapper reportMapper;

    public ResponseEntity<ApiResponse<ReportAnalyticsDto>> getAnalytics() {

        ReportAnalyticsDto analytics =
                reportMapper.toAnalyticsDto(
                        userRepository.findAll(),
                        departmentRepository.findAll(),
                        jobRepository.findAll(),
                        applicationRepository.findAll(),
                        interviewRepository.findAll()
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Report analytics fetched successfully",
                        analytics
                )
        );
    }
}