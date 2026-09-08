package com.ems.controller;

import com.ems.common.ApiResponse;
import com.ems.dto.AICandidateMatchingRequestDto;
import com.ems.dto.AICandidateMatchingResponseDto;
import com.ems.service.AICandidateMatchingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai-candidate-matching")
public class AICandidateMatchingController {

    @Autowired
    private AICandidateMatchingService matchingService;


    @PostMapping("/match")
    public ResponseEntity<ApiResponse<AICandidateMatchingResponseDto>> matchCandidate(
            @Valid @RequestBody AICandidateMatchingRequestDto requestDto) {

        return matchingService.matchCandidate(requestDto);
    }


    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<AICandidateMatchingResponseDto>>> getAllMatches() {

        return matchingService.getAllMatches();
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AICandidateMatchingResponseDto>> getMatchById(
            @PathVariable Long id) {

        return matchingService.getMatchById(id);
    }
}