package com.ems.mapper;

import com.ems.dto.JobRequestDto;
import com.ems.dto.JobResponseDto;
import com.ems.entity.Job;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JobMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    Job toEntity(JobRequestDto jobRequestDto);

    JobResponseDto toResponseDto(Job job);
}