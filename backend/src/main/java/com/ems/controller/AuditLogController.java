package com.ems.controller;

import com.ems.entity.AuditLog;
import com.ems.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    // Create audit log
    @PostMapping("/create")
    public ResponseEntity<AuditLog> createAuditLog(
            @RequestBody AuditLog auditLog) {

        AuditLog createdLog =
                auditLogService.createAuditLog(auditLog);

        return ResponseEntity.ok(createdLog);
    }

    // Get all audit logs
    @GetMapping("/all")
    public ResponseEntity<?> getAllAuditLogs() {

        return ResponseEntity.ok(
                auditLogService.getAllAuditLogs()
        );
    }

    // Search/filter audit logs
    @GetMapping("/search")
    public ResponseEntity<?> searchAuditLogs(
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String date) {

        LocalDate parsedDate = null;

        if (date != null && !date.isBlank()) {
            parsedDate = LocalDate.parse(date);
        }

        return ResponseEntity.ok(
                auditLogService.searchAuditLogs(
                        module,
                        action,
                        parsedDate
                )
        );
    }

    // Get audit log by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getAuditLogById(
            @PathVariable Long id) {

        AuditLog auditLog =
                auditLogService.getAuditLogById(id);

        if (auditLog == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(auditLog);
    }
}