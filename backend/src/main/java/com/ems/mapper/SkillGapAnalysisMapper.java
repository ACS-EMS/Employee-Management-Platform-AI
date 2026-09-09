package com.ems.mapper;

import com.ems.dto.SkillGapAnalysisResponseDto;
import com.ems.entity.SkillGapAnalysis;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SkillGapAnalysisMapper {

    SkillGapAnalysisResponseDto toResponseDto(
            SkillGapAnalysis skillGapAnalysis
    );
}