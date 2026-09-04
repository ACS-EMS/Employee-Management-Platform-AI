package com.ems.service;

import com.ems.common.ApiResponse;
import com.ems.dto.InterviewRequestDto;
import com.ems.entity.Application;
import com.ems.entity.Interview;
import com.ems.entity.User;
import com.ems.repository.ApplicationRepository;
import com.ems.repository.InterviewRepository;
import com.ems.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InterviewService {

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;


    // Employer schedules interview
    public ResponseEntity<ApiResponse<Interview>> scheduleInterview(
            InterviewRequestDto dto) {

        try {

            Application application = applicationRepository
                    .findById(dto.getApplicationId())
                    .orElseThrow(() ->
                            new RuntimeException("Application not found")
                    );

            Interview interview = new Interview();

            interview.setApplicationId(
                    application.getApplicationId()
            );

            interview.setCandidateId(
                    application.getCandidateId()
            );

            interview.setJobId(
                    application.getJobId()
            );

            interview.setInterviewDateTime(
                    dto.getInterviewDateTime()
            );

            interview.setInterviewMode(
                    dto.getInterviewMode()
            );

            interview.setMeetingLink(
                    dto.getMeetingLink()
            );

            interview.setLocation(
                    dto.getLocation()
            );

            interview.setNotes(
                    dto.getNotes()
            );

            interview.setStatus("SCHEDULED");

            interview.setCreatedAt(
                    LocalDateTime.now()
            );

            Interview savedInterview =
                    interviewRepository.save(interview);

            ApiResponse<Interview> response =
                    new ApiResponse<>(
                            true,
                            "Interview scheduled successfully",
                            savedInterview
                    );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.CREATED
            );

        } catch (Exception e) {

            ApiResponse<Interview> response =
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


    // Candidate views own interviews
    public ResponseEntity<ApiResponse<List<Interview>>> getMyInterviews(
            Authentication authentication) {

        try {

            String email = authentication.getName();

            User candidate = userRepository
                    .findByEmailIgnoreCase(email)
                    .orElseThrow(() ->
                            new RuntimeException("Candidate not found")
                    );

            List<Interview> interviews =
                    interviewRepository.findByCandidateId(
                            candidate.getUserId()
                    );

            ApiResponse<List<Interview>> response =
                    new ApiResponse<>(
                            true,
                            "Interviews fetched successfully",
                            interviews
                    );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.OK
            );

        } catch (Exception e) {

            ApiResponse<List<Interview>> response =
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


    // Employer views interviews for a particular job
    public ResponseEntity<ApiResponse<List<Interview>>> getInterviewsByJob(
            Long jobId) {

        try {

            List<Interview> interviews =
                    interviewRepository.findByJobId(jobId);

            ApiResponse<List<Interview>> response =
                    new ApiResponse<>(
                            true,
                            "Interviews fetched successfully",
                            interviews
                    );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.OK
            );

        } catch (Exception e) {

            ApiResponse<List<Interview>> response =
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


    // Employer reschedules interview
    public ResponseEntity<ApiResponse<Interview>> rescheduleInterview(
            Long interviewId,
            InterviewRequestDto dto) {

        try {

            Interview interview =
                    interviewRepository
                            .findById(interviewId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Interview not found"
                                    )
                            );

            interview.setInterviewDateTime(
                    dto.getInterviewDateTime()
            );

            interview.setInterviewMode(
                    dto.getInterviewMode()
            );

            interview.setMeetingLink(
                    dto.getMeetingLink()
            );

            interview.setLocation(
                    dto.getLocation()
            );

            interview.setNotes(
                    dto.getNotes()
            );

            interview.setStatus("RESCHEDULED");

            Interview updatedInterview =
                    interviewRepository.save(interview);

            ApiResponse<Interview> response =
                    new ApiResponse<>(
                            true,
                            "Interview rescheduled successfully",
                            updatedInterview
                    );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.OK
            );

        } catch (Exception e) {

            ApiResponse<Interview> response =
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


    // Employer updates interview status
    public ResponseEntity<ApiResponse<Interview>> updateInterviewStatus(
            Long interviewId,
            String status) {

        try {

            Interview interview =
                    interviewRepository
                            .findById(interviewId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Interview not found"
                                    )
                            );

            interview.setStatus(status);

            Interview updatedInterview =
                    interviewRepository.save(interview);

            ApiResponse<Interview> response =
                    new ApiResponse<>(
                            true,
                            "Interview status updated successfully",
                            updatedInterview
                    );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.OK
            );

        } catch (Exception e) {

            ApiResponse<Interview> response =
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