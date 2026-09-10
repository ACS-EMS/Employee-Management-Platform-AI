package com.ems.service;

import com.ems.entity.AuditLog;
import com.ems.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    // Create an audit log
    public AuditLog createAuditLog(AuditLog auditLog) {

        if (auditLog.getDateTime() == null) {
            auditLog.setDateTime(LocalDateTime.now());
        }

        return auditLogRepository.save(auditLog);
    }

    // Get all audit logs
    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }

    // Get audit log by ID
    public AuditLog getAuditLogById(Long id) {
        return auditLogRepository.findById(id).orElse(null);
    }

    // Search/filter audit logs
    public List<AuditLog> searchAuditLogs(
            String module,
            String action,
            LocalDate date) {

        List<AuditLog> logs = auditLogRepository.findAll();

        return logs.stream()
                .filter(log ->
                        module == null
                                || module.isBlank()
                                || (log.getModule() != null
                                && log.getModule()
                                .toLowerCase()
                                .contains(module.toLowerCase()))
                )
                .filter(log ->
                        action == null
                                || action.isBlank()
                                || (log.getAction() != null
                                && log.getAction()
                                .equalsIgnoreCase(action))
                )
                .filter(log ->
                        date == null
                                || (log.getDateTime() != null
                                && log.getDateTime().toLocalDate()
                                .equals(date))
                )
                .toList();
    }
}