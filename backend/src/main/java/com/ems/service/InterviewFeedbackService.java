package com.ems.service;

import com.ems.dto.InterviewFeedbackDto;
import com.ems.entity.InterviewFeedback;
import com.ems.entity.User;
import com.ems.mapper.InterviewFeedbackMapper;
import com.ems.repository.InterviewFeedbackRepository;
import com.ems.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ems.common.ApiResponse;
import com.ems.entity.Interview;
import com.ems.repository.InterviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InterviewFeedbackService {

    @Autowired
    private InterviewFeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private InterviewRepository interviewRepository;
    @Autowired
    private InterviewFeedbackMapper interviewFeedbackMapper;
    public ResponseEntity<ApiResponse<InterviewFeedback>> submitFeedback(
            InterviewFeedbackDto dto,
            Authentication authentication) {

        try {

            String email = authentication.getName();

            User loggedInUser = userRepository
                    .findByEmailIgnoreCase(email)
                    .orElseThrow(() ->
                            new RuntimeException("User not found")
                    );

            Interview interview = interviewRepository
                    .findById(dto.getInterviewId())
                    .orElseThrow(() ->
                            new RuntimeException("Interview not found")
                    );

            if (interview.getInterviewerId() == null) {

                ApiResponse<InterviewFeedback> response =
                        new ApiResponse<>(
                                false,
                                "No interviewer assigned to this interview",
                                null
                        );

                return new ResponseEntity<>(
                        response,
                        HttpStatus.BAD_REQUEST
                );
            }

            if (!interview.getInterviewerId()
                    .equals(loggedInUser.getUserId())) {

                ApiResponse<InterviewFeedback> response =
                        new ApiResponse<>(
                                false,
                                "You are not assigned to this interview",
                                null
                        );

                return new ResponseEntity<>(
                        response,
                        HttpStatus.FORBIDDEN
                );
            }

            if (feedbackRepository.existsByInterviewId(
                    dto.getInterviewId())) {

                ApiResponse<InterviewFeedback> response =
                        new ApiResponse<>(
                                false,
                                "Feedback already submitted for this interview",
                                null
                        );

                return new ResponseEntity<>(
                        response,
                        HttpStatus.BAD_REQUEST
                );
            }

            if (dto.getRating() == null
                    || dto.getRating() < 1
                    || dto.getRating() > 5) {

                ApiResponse<InterviewFeedback> response =
                        new ApiResponse<>(
                                false,
                                "Rating must be between 1 and 5",
                                null
                        );

                return new ResponseEntity<>(
                        response,
                        HttpStatus.BAD_REQUEST
                );
            }

            InterviewFeedback feedback =
                    interviewFeedbackMapper.toEntity(dto);

            feedback.setInterviewerId(
                    loggedInUser.getUserId()
            );

            feedback.setSubmittedAt(
                    LocalDateTime.now()
            );

            InterviewFeedback savedFeedback =
                    feedbackRepository.save(feedback);

            List<User> hrUsers =
                    userRepository.findByRole("HR");

            for (User hr : hrUsers) {

                notificationService.createNotification(
                        hr.getUserId(),
                        "Interview feedback has been submitted for interview ID "
                                + dto.getInterviewId(),
                        "FEEDBACK_SUBMITTED"
                );
            }

            ApiResponse<InterviewFeedback> response =
                    new ApiResponse<>(
                            true,
                            "Interview feedback submitted successfully",
                            savedFeedback
                    );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.CREATED
            );

        } catch (Exception e) {

            ApiResponse<InterviewFeedback> response =
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


    public List<InterviewFeedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }


    public InterviewFeedback getFeedbackByInterview(
            Long interviewId) {

        return feedbackRepository
                .findByInterviewId(interviewId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Feedback not found for interview ID: "
                                        + interviewId
                        )
                );
    }
}