package com.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpcomingInterviewDto {

    private Long interviewId;
    private Long candidateId;
    private Long jobId;
    private Long applicationId;
    private LocalDateTime interviewDateTime;
    private String interviewMode;
    private String meetingLink;
    private String status;
}