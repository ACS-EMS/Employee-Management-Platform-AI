package com.ems.controller;

import com.ems.common.ApiResponse;
import com.ems.dto.ReportAnalyticsDto;
import com.ems.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/analytics")
    public ResponseEntity<ApiResponse<ReportAnalyticsDto>> getAnalytics() {
        return reportService.getAnalytics();
    }
}