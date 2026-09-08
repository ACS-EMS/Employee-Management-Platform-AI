package com.ems.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResumeRequestDto {

    @NotNull(message = "Candidate ID is required")
    private Long candidateId;
}