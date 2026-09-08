package com.ems.mapper;

import com.ems.dto.ResumeRequestDto;
import com.ems.dto.ResumeResponseDto;
import com.ems.entity.Resume;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ResumeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fileName", ignore = true)
    @Mapping(target = "fileType", ignore = true)
    @Mapping(target = "filePath", ignore = true)
    @Mapping(target = "extractedText", ignore = true)
    @Mapping(target = "extractedSkills", ignore = true)
    @Mapping(target = "extractedExperience", ignore = true)
    @Mapping(target = "extractedEducation", ignore = true)
    Resume toEntity(ResumeRequestDto resumeRequestDto);

    ResumeResponseDto toResponseDto(Resume resume);
}