package com.ems.controller;

import com.ems.common.ApiResponse;
import com.ems.dto.AIResumeScreeningRequestDto;
import com.ems.dto.AIResumeScreeningResponseDto;
import com.ems.service.AIResumeScreeningService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai-resume-screening")
public class AIResumeScreeningController {

    @Autowired
    private AIResumeScreeningService screeningService;

    @PostMapping("/screen")
    public ResponseEntity<ApiResponse<AIResumeScreeningResponseDto>> screenResume(
            @Valid @RequestBody AIResumeScreeningRequestDto requestDto) {

        return screeningService.screenResume(requestDto);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<AIResumeScreeningResponseDto>>> getAllScreenings() {

        return screeningService.getAllScreenings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AIResumeScreeningResponseDto>> getScreeningById(
            @PathVariable Long id) {

        return screeningService.getScreeningById(id);
    }
}