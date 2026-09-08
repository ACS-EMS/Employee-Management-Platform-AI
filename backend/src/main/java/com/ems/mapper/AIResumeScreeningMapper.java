package com.ems.mapper;

import com.ems.dto.AIResumeScreeningRequestDto;
import com.ems.dto.AIResumeScreeningResponseDto;
import com.ems.entity.AIResumeScreening;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AIResumeScreeningMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "skills", ignore = true)
    @Mapping(target = "experience", ignore = true)
    @Mapping(target = "education", ignore = true)
    @Mapping(target = "certifications", ignore = true)
    @Mapping(target = "jobTitles", ignore = true)
    @Mapping(target = "companies", ignore = true)
    @Mapping(target = "technologies", ignore = true)
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "yearsOfExperience", ignore = true)
    @Mapping(target = "screeningScore", ignore = true)
    @Mapping(target = "screeningResult", ignore = true)
    @Mapping(target = "recommendation", ignore = true)
    AIResumeScreening toEntity(AIResumeScreeningRequestDto requestDto);

    AIResumeScreeningResponseDto toResponseDto(AIResumeScreening screening);
}