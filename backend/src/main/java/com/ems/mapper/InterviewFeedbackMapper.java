package com.ems.mapper;

import com.ems.dto.InterviewFeedbackDto;
import com.ems.entity.InterviewFeedback;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface InterviewFeedbackMapper {
    @Mapping(target = "feedbackId", ignore = true)
    @Mapping(target = "submittedAt", ignore = true)
    InterviewFeedback toEntity(
            InterviewFeedbackDto dto
    );

    InterviewFeedbackDto toDto(
            InterviewFeedback feedback
    );
}
