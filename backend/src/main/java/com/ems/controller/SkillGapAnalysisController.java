package com.ems.controller;

import com.ems.common.ApiResponse;
import com.ems.dto.SkillGapAnalysisRequestDto;
import com.ems.dto.SkillGapAnalysisResponseDto;
import com.ems.service.SkillGapAnalysisService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skill-gap-analysis")
public class SkillGapAnalysisController {

    @Autowired
    private SkillGapAnalysisService skillGapAnalysisService;


    @PostMapping("/analyze")
    public ResponseEntity<ApiResponse<SkillGapAnalysisResponseDto>> analyzeSkillGap(
            @Valid @RequestBody SkillGapAnalysisRequestDto requestDto) {

        return skillGapAnalysisService.analyzeSkillGap(requestDto);
    }


    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<SkillGapAnalysisResponseDto>>> getAllAnalyses() {

        return skillGapAnalysisService.getAllAnalyses();
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SkillGapAnalysisResponseDto>> getAnalysisById(
            @PathVariable Long id) {

        return skillGapAnalysisService.getAnalysisById(id);
    }


    @GetMapping("/job/{jobId}")
    public ResponseEntity<ApiResponse<List<SkillGapAnalysisResponseDto>>> getAnalysesByJob(
            @PathVariable Long jobId) {

        return skillGapAnalysisService.getAnalysesByJob(jobId);
    }
}