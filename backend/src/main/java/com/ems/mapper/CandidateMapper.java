package com.ems.mapper;

import com.ems.dto.CandidateRequestDto;
import com.ems.dto.CandidateResponseDto;
import com.ems.entity.Candidate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CandidateMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "applicationDate", ignore = true)
    Candidate toEntity(CandidateRequestDto candidateRequestDto);

    CandidateResponseDto toResponseDto(Candidate candidate);
}