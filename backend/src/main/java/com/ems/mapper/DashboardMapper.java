package com.ems.mapper;

import com.ems.dto.RecentApplicationDto;
import com.ems.dto.RecentJobDto;
import com.ems.dto.TopCandidateDto;
import com.ems.dto.UpcomingInterviewDto;
import com.ems.entity.AIResumeScreening;
import com.ems.entity.Application;
import com.ems.entity.Interview;
import com.ems.entity.Job;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DashboardMapper {

    RecentApplicationDto toRecentApplicationDto(
            Application application
    );

    TopCandidateDto toTopCandidateDto(
            AIResumeScreening screening
    );
    RecentJobDto toRecentJobDto(Job job);

    @Mapping(
            target = "status",
            expression = "java(interview.getStatus() != null ? interview.getStatus().name() : null)"
    )
    UpcomingInterviewDto toUpcomingInterviewDto(
            Interview interview
    );
}