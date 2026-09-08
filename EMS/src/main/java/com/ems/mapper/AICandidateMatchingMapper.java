package com.ems.mapper;

import com.ems.dto.AICandidateMatchingRequestDto;
import com.ems.dto.AICandidateMatchingResponseDto;
import com.ems.entity.AICandidateMatching;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AICandidateMatchingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "skillMatch", ignore = true)
    @Mapping(target = "experienceMatch", ignore = true)
    @Mapping(target = "educationMatch", ignore = true)
    @Mapping(target = "certificationMatch", ignore = true)
    @Mapping(target = "locationMatch", ignore = true)
    @Mapping(target = "roleMatch", ignore = true)
    @Mapping(target = "overallMatchScore", ignore = true)
    @Mapping(target = "explanation", ignore = true)
    AICandidateMatching toEntity(AICandidateMatchingRequestDto requestDto);

    AICandidateMatchingResponseDto toResponseDto(AICandidateMatching matching);
}