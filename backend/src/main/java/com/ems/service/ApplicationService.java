package com.ems.service;

import com.ems.common.ApiResponse;
import com.ems.entity.Application;
import com.ems.entity.Job;
import com.ems.entity.User;
import com.ems.common.ApplicationStatus;
import com.ems.repository.ApplicationRepository;
import com.ems.repository.JobRepository;
import com.ems.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;


    // Candidate applies for a job
    public ResponseEntity<ApiResponse<Application>> applyForJob(
            Long jobId,
            Authentication authentication) {

        try {

            String email = authentication.getName();

            User candidate = userRepository
                    .findByEmailIgnoreCase(email)
                    .orElseThrow(() ->
                            new RuntimeException("Candidate not found")
                    );

            Job job = jobRepository
                    .findById(jobId)
                    .orElseThrow(() ->
                            new RuntimeException("Job not found")
                    );

            boolean alreadyApplied =
                    applicationRepository
                            .existsByCandidateIdAndJobId(
                                    candidate.getUserId(),
                                    job.getId()
                            );

            if (alreadyApplied) {

                ApiResponse<Application> response =
                        new ApiResponse<>(
                                false,
                                "You have already applied for this job",
                                null
                        );

                return new ResponseEntity<>(
                        response,
                        HttpStatus.CONFLICT
                );
            }

            Application application =
                    new Application();

            application.setCandidateId(
                    candidate.getUserId()
            );

            application.setJobId(
                    job.getId()
            );

            application.setStatus(
                    ApplicationStatus.APPLIED
            );

            application.setAppliedAt(
                    LocalDateTime.now()
            );

            Application savedApplication =
                    applicationRepository.save(application);

            ApiResponse<Application> response =
                    new ApiResponse<>(
                            true,
                            "Job applied successfully",
                            savedApplication
                    );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.CREATED
            );

        } catch (Exception e) {

            ApiResponse<Application> response =
                    new ApiResponse<>(
                            false,
                            e.getMessage(),
                            null
                    );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.BAD_REQUEST
            );
        }
    }


    // Candidate views own applications
    public ResponseEntity<ApiResponse<List<Application>>> getMyApplications(
            Authentication authentication) {

        try {

            String email = authentication.getName();

            User candidate = userRepository
                    .findByEmailIgnoreCase(email)
                    .orElseThrow(() ->
                            new RuntimeException("Candidate not found")
                    );

            List<Application> applications =
                    applicationRepository
                            .findByCandidateId(
                                    candidate.getUserId()
                            );

            ApiResponse<List<Application>> response =
                    new ApiResponse<>(
                            true,
                            "Applications fetched successfully",
                            applications
                    );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.OK
            );

        } catch (Exception e) {

            ApiResponse<List<Application>> response =
                    new ApiResponse<>(
                            false,
                            e.getMessage(),
                            null
                    );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.BAD_REQUEST
            );
        }
    }


    // Employer views applications for a particular job
    public ResponseEntity<ApiResponse<List<Application>>> getApplicationsByJob(
            Long jobId) {

        try {

            Job job = jobRepository
                    .findById(jobId)
                    .orElseThrow(() ->
                            new RuntimeException("Job not found")
                    );

            List<Application> applications =
                    applicationRepository
                            .findByJobId(job.getId());

            ApiResponse<List<Application>> response =
                    new ApiResponse<>(
                            true,
                            "Applications fetched successfully",
                            applications
                    );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.OK
            );

        } catch (Exception e) {

            ApiResponse<List<Application>> response =
                    new ApiResponse<>(
                            false,
                            e.getMessage(),
                            null
                    );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.BAD_REQUEST
            );
        }
    }


    // Employer updates application status
    public ResponseEntity<ApiResponse<Application>> updateStatus(
            Long applicationId,
            ApplicationStatus status) {

        try {

            Application application =
                    applicationRepository
                            .findById(applicationId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Application not found"
                                    )
                            );

            application.setStatus(status);

            Application updatedApplication =
                    applicationRepository.save(application);

            ApiResponse<Application> response =
                    new ApiResponse<>(
                            true,
                            "Application status updated successfully",
                            updatedApplication
                    );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.OK
            );

        } catch (Exception e) {

            ApiResponse<Application> response =
                    new ApiResponse<>(
                            false,
                            e.getMessage(),
                            null
                    );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    public ResponseEntity<ApiResponse<Application>> withdrawApplication(
            Long applicationId,
            Authentication authentication) {

        try {

            String email = authentication.getName();

            User candidate = userRepository
                    .findByEmailIgnoreCase(email)
                    .orElseThrow(() ->
                            new RuntimeException("Candidate not found")
                    );

            Application application = applicationRepository
                    .findById(applicationId)
                    .orElseThrow(() ->
                            new RuntimeException("Application not found")
                    );

            if (!application.getCandidateId().equals(candidate.getUserId())) {

                ApiResponse<Application> response =
                        new ApiResponse<>(
                                false,
                                "You cannot withdraw another candidate's application",
                                null
                        );

                return new ResponseEntity<>(
                        response,
                        HttpStatus.FORBIDDEN
                );
            }

            if (application.getStatus() == ApplicationStatus.WITHDRAWN) {

                ApiResponse<Application> response =
                        new ApiResponse<>(
                                false,
                                "Application is already withdrawn",
                                null
                        );

                return new ResponseEntity<>(
                        response,
                        HttpStatus.BAD_REQUEST
                );
            }

            application.setStatus(ApplicationStatus.WITHDRAWN);

            Application updatedApplication =
                    applicationRepository.save(application);

            ApiResponse<Application> response =
                    new ApiResponse<>(
                            true,
                            "Application withdrawn successfully",
                            updatedApplication
                    );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.OK
            );

        } catch (Exception e) {

            ApiResponse<Application> response =
                    new ApiResponse<>(
                            false,
                            e.getMessage(),
                            null
                    );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}