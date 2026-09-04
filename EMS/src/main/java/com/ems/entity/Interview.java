package com.ems.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "interviews")
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interviewId;

    private Long applicationId;

    private Long candidateId;

    private Long jobId;

    private LocalDateTime interviewDateTime;

    private String interviewMode;

    private String meetingLink;

    private String location;

    private String status;

    private String notes;

    private LocalDateTime createdAt;
}