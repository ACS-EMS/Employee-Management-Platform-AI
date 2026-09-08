package com.ems.controller;

import com.ems.common.ApiResponse;
import com.ems.entity.Notification;
import com.ems.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<Notification>>> getMyNotifications(
            Authentication authentication) {

        return notificationService.getMyNotifications(authentication);
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<Notification>> markAsRead(
            @PathVariable Long notificationId,
            Authentication authentication) {

        return notificationService.markAsRead(
                notificationId,
                authentication
        );
    }
}