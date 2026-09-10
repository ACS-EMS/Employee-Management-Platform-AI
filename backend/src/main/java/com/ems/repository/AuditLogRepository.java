package com.ems.repository;

import com.ems.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditLogRepository
        extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByModuleContainingIgnoreCase(String module);

    List<AuditLog> findByActionIgnoreCase(String action);

    List<AuditLog> findByDateTimeBetween(
            LocalDateTime start,
            LocalDateTime end
    );
}