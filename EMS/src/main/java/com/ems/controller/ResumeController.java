package com.ems.controller;

import com.ems.common.ApiResponse;
import com.ems.dto.ResumeRequestDto;
import com.ems.dto.ResumeResponseDto;
import com.ems.service.ResumeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;


    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<ResumeResponseDto>> uploadResume(
            @Valid @ModelAttribute ResumeRequestDto resumeRequestDto,
            @RequestParam("file") MultipartFile file) {

        return resumeService.uploadResume(
                resumeRequestDto,
                file
        );
    }


    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ResumeResponseDto>> createResume(
            @Valid @RequestBody ResumeRequestDto resumeRequestDto) {

        return resumeService.createResume(
                resumeRequestDto
        );
    }


    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ResumeResponseDto>>> getAllResumes() {

        return resumeService.getAllResumes();
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResumeResponseDto>> getResumeById(
            @PathVariable Long id) {

        return resumeService.getResumeById(id);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ResumeResponseDto>> updateResume(
            @PathVariable Long id,
            @Valid @RequestBody ResumeRequestDto resumeRequestDto) {

        return resumeService.updateResume(
                id,
                resumeRequestDto
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteResume(
            @PathVariable Long id) {

        return resumeService.deleteResume(id);
    }
}