package com.ems.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewFeedbackDto {

    private Long interviewId;

    private Integer rating;
    private String feedback;
    private String recommendation;
}