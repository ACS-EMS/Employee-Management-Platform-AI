package com.ems.service;

import com.ems.dto.InterviewFeedbackDto;
import com.ems.entity.InterviewFeedback;
import com.ems.repository.InterviewFeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InterviewFeedbackService {

    @Autowired
    private InterviewFeedbackRepository feedbackRepository;

    public InterviewFeedback submitFeedback(InterviewFeedbackDto dto) {

        if (feedbackRepository.existsByInterviewId(dto.getInterviewId())) {
            throw new RuntimeException(
                    "Feedback already submitted for this interview"
            );
        }

        if (dto.getRating() < 1 || dto.getRating() > 5) {
            throw new RuntimeException(
                    "Rating must be between 1 and 5"
            );
        }

        InterviewFeedback feedback = InterviewFeedback.builder()
                .interviewId(dto.getInterviewId())
                .interviewerId(dto.getInterviewerId())
                .rating(dto.getRating())
                .feedback(dto.getFeedback())
                .recommendation(dto.getRecommendation())
                .submittedAt(LocalDateTime.now())
                .build();

        return feedbackRepository.save(feedback);
    }

    public InterviewFeedback getFeedbackByInterview(Long interviewId) {

        return feedbackRepository
                .findByInterviewId(interviewId)
                .orElseThrow(() ->
                        new RuntimeException("Feedback not found"));
    }

    public List<InterviewFeedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }
}