package com.ems.dto;

import com.ems.common.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecentApplicationDto {

    private Long applicationId;
    private Long candidateId;
    private Long jobId;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;
}