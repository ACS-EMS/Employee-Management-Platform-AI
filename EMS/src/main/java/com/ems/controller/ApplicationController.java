package com.ems.controller;

import com.ems.common.ApiResponse;
import com.ems.entity.Application;
import com.ems.common.ApplicationStatus;
import com.ems.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "http://localhost:5173")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;


    @PostMapping("/apply/{jobId}")
    public ResponseEntity<ApiResponse<Application>> applyForJob(
            @PathVariable Long jobId,
            Authentication authentication) {

        return applicationService.applyForJob(
                jobId,
                authentication
        );
    }


    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<Application>>> getMyApplications(
            Authentication authentication) {

        return applicationService
                .getMyApplications(authentication);
    }


    @GetMapping("/job/{jobId}")
    public ResponseEntity<ApiResponse<List<Application>>> getApplicationsByJob(
            @PathVariable Long jobId) {

        return applicationService
                .getApplicationsByJob(jobId);
    }


    @PutMapping("/{applicationId}/status")
    public ResponseEntity<ApiResponse<Application>> updateStatus(
            @PathVariable Long applicationId,
            @RequestParam ApplicationStatus status) {

        return applicationService
                .updateStatus(
                        applicationId,
                        status
                );
    }
    @PutMapping("/{applicationId}/withdraw")
    public ResponseEntity<ApiResponse<Application>> withdrawApplication(
            @PathVariable Long applicationId,
            Authentication authentication) {

        return applicationService.withdrawApplication(
                applicationId,
                authentication
        );
    }
}