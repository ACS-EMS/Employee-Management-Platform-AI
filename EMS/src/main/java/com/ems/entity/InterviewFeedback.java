package com.ems.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "interview_feedback")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;

    @Column(nullable = false)
    private Long interviewId;

    @Column(nullable = false)
    private Long interviewerId;

    @Column(nullable = false)
    private Integer rating;

    @Column(length = 2000)
    private String feedback;

    @Column(nullable = false)
    private String recommendation;

    private LocalDateTime submittedAt;
}