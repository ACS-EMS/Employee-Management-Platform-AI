package com.ems.service;

import com.ems.common.ApiResponse;
import com.ems.dto.SkillGapAnalysisRequestDto;
import com.ems.dto.SkillGapAnalysisResponseDto;
import com.ems.entity.Job;
import com.ems.entity.Resume;
import com.ems.entity.SkillGapAnalysis;
import com.ems.mapper.SkillGapAnalysisMapper;
import com.ems.repository.JobRepository;
import com.ems.repository.ResumeRepository;
import com.ems.repository.SkillGapAnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class SkillGapAnalysisService {

    @Autowired
    private SkillGapAnalysisRepository skillGapAnalysisRepository;

    @Autowired
    private SkillGapAnalysisMapper skillGapAnalysisMapper;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private JobRepository jobRepository;


    public ResponseEntity<ApiResponse<SkillGapAnalysisResponseDto>> analyzeSkillGap(
            SkillGapAnalysisRequestDto requestDto) {

        try {

            Resume resume = resumeRepository
                    .findByCandidateId(requestDto.getCandidateId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Resume not found for candidate ID: "
                                            + requestDto.getCandidateId()
                            )
                    );


            Job job = jobRepository
                    .findById(requestDto.getJobId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Job not found with ID: "
                                            + requestDto.getJobId()
                            )
                    );


            String candidateSkills =
                    safeText(resume.getExtractedSkills());

            String requiredSkills =
                    safeText(job.getRequiredSkills());


            List<String> candidateSkillList =
                    splitSkills(candidateSkills);

            List<String> requiredSkillList =
                    splitSkills(requiredSkills);


            List<String> matchingSkills =
                    new ArrayList<>();

            List<String> missingSkills =
                    new ArrayList<>();


            for (String requiredSkill : requiredSkillList) {

                boolean matched =
                        candidateSkillList.stream()
                                .anyMatch(candidateSkill ->
                                        skillsMatch(
                                                candidateSkill,
                                                requiredSkill
                                        )
                                );

                if (matched) {

                    matchingSkills.add(requiredSkill);

                } else {

                    missingSkills.add(requiredSkill);
                }
            }


            double skillMatchPercentage = 0.0;

            if (!requiredSkillList.isEmpty()) {

                skillMatchPercentage =
                        (matchingSkills.size() * 100.0)
                                / requiredSkillList.size();
            }


            skillMatchPercentage =
                    roundScore(skillMatchPercentage);


            SkillGapAnalysis analysis =
                    new SkillGapAnalysis();

            analysis.setCandidateId(
                    requestDto.getCandidateId()
            );

            analysis.setJobId(
                    requestDto.getJobId()
            );

            analysis.setCandidateSkills(
                    candidateSkills
            );

            analysis.setRequiredSkills(
                    requiredSkills
            );

            analysis.setMatchingSkills(
                    String.join(", ", matchingSkills)
            );

            analysis.setMissingSkills(
                    String.join(", ", missingSkills)
            );

            analysis.setSkillMatchPercentage(
                    skillMatchPercentage
            );


            SkillGapAnalysis savedAnalysis =
                    skillGapAnalysisRepository.save(analysis);


            SkillGapAnalysisResponseDto responseDto =
                    skillGapAnalysisMapper.toResponseDto(
                            savedAnalysis
                    );


            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(
                            new ApiResponse<>(
                                    true,
                                    "Skill gap analysis completed successfully",
                                    responseDto
                            )
                    );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Failed to perform skill gap analysis: "
                                            + e.getMessage(),
                                    null
                            )
                    );
        }
    }


    private List<String> splitSkills(String skills) {

        if (skills == null || skills.isBlank()) {
            return new ArrayList<>();
        }

        return java.util.Arrays.stream(
                        skills
                                .toLowerCase(Locale.ROOT)
                                .split("[,;|]")
                )
                .map(String::trim)
                .filter(skill -> !skill.isBlank())
                .distinct()
                .collect(Collectors.toList());
    }


    private boolean skillsMatch(
            String candidateSkill,
            String requiredSkill) {

        String candidate =
                candidateSkill
                        .toLowerCase(Locale.ROOT)
                        .trim();

        String required =
                requiredSkill
                        .toLowerCase(Locale.ROOT)
                        .trim();


        if (candidate.equals(required)) {
            return true;
        }


        return candidate.contains(required)
                || required.contains(candidate);
    }


    private String safeText(String value) {

        return value == null ? "" : value.trim();
    }


    private double roundScore(double value) {

        return Math.round(value * 100.0) / 100.0;
    }


    public ResponseEntity<ApiResponse<List<SkillGapAnalysisResponseDto>>> getAllAnalyses() {

        try {

            List<SkillGapAnalysisResponseDto> analyses =
                    skillGapAnalysisRepository
                            .findAll()
                            .stream()
                            .map(skillGapAnalysisMapper::toResponseDto)
                            .collect(Collectors.toList());


            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Skill gap analyses retrieved successfully",
                            analyses
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Failed to retrieve skill gap analyses: "
                                            + e.getMessage(),
                                    null
                            )
                    );
        }
    }


    public ResponseEntity<ApiResponse<SkillGapAnalysisResponseDto>> getAnalysisById(
            Long id) {

        try {

            SkillGapAnalysis analysis =
                    skillGapAnalysisRepository
                            .findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Skill gap analysis not found with ID: "
                                                    + id
                                    )
                            );


            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Skill gap analysis retrieved successfully",
                            skillGapAnalysisMapper.toResponseDto(
                                    analysis
                            )
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Failed to retrieve skill gap analysis: "
                                            + e.getMessage(),
                                    null
                            )
                    );
        }
    }


    public ResponseEntity<ApiResponse<List<SkillGapAnalysisResponseDto>>> getAnalysesByJob(
            Long jobId) {

        try {

            List<SkillGapAnalysisResponseDto> analyses =
                    skillGapAnalysisRepository
                            .findByJobId(jobId)
                            .stream()
                            .map(skillGapAnalysisMapper::toResponseDto)
                            .collect(Collectors.toList());


            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Skill gap analyses for job retrieved successfully",
                            analyses
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Failed to retrieve skill gap analyses: "
                                            + e.getMessage(),
                                    null
                            )
                    );
        }
    }
}