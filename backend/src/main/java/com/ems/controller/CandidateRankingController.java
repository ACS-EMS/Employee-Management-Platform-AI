package com.ems.controller;

import com.ems.common.ApiResponse;
import com.ems.dto.CandidateRankingRequestDto;
import com.ems.dto.CandidateRankingResponseDto;
import com.ems.service.CandidateRankingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidate-ranking")
public class CandidateRankingController {

    @Autowired
    private CandidateRankingService rankingService;


    @PostMapping("/rank")
    public ResponseEntity<ApiResponse<List<CandidateRankingResponseDto>>> rankCandidates(
            @Valid @RequestBody CandidateRankingRequestDto requestDto) {

        return rankingService.rankCandidates(requestDto);
    }


    @GetMapping("/job/{jobId}")
    public ResponseEntity<ApiResponse<List<CandidateRankingResponseDto>>> getRankingsByJob(
            @PathVariable Long jobId) {

        return rankingService.getRankingsByJob(jobId);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CandidateRankingResponseDto>> getRankingById(
            @PathVariable Long id) {

        return rankingService.getRankingById(id);
    }
}