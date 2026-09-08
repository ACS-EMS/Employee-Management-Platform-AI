package com.ems.controller;

import com.ems.common.ApiResponse;
import com.ems.dto.DashboardSummaryDto;
import com.ems.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:5173")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<DashboardSummaryDto>>
    getDashboardSummary() {

        return dashboardService.getDashboardSummary();
    }
}