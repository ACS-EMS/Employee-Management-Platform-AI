package com.ems.mapper;

import com.ems.dto.CandidateRankingResponseDto;
import com.ems.entity.CandidateRanking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CandidateRankingMapper {

    CandidateRankingResponseDto toResponseDto(CandidateRanking ranking);
}