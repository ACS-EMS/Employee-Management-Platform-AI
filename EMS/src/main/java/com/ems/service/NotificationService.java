package com.ems.service;

import com.ems.common.ApiResponse;
import com.ems.entity.Notification;
import com.ems.entity.User;
import com.ems.repository.NotificationRepository;
import com.ems.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(
            NotificationRepository notificationRepository,
            UserRepository userRepository) {

        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }


    // =========================
    // CREATE NOTIFICATION
    // =========================

    public Notification createNotification(
            Long userId,
            String message,
            String type) {

        Notification notification = Notification.builder()
                .userId(userId)
                .message(message)
                .type(type)
                .readStatus(false)
                .createdAt(LocalDateTime.now())
                .build();

        return notificationRepository.save(notification);
    }


    // =========================
    // GET MY NOTIFICATIONS
    // =========================

    public ResponseEntity<ApiResponse<List<Notification>>> getMyNotifications(
            Authentication authentication) {

        try {

            String email = authentication.getName();

            User user = userRepository
                    .findByEmailIgnoreCase(email)
                    .orElseThrow(() ->
                            new RuntimeException("User not found")
                    );

            List<Notification> notifications =
                    notificationRepository
                            .findByUserIdOrderByCreatedAtDesc(
                                    user.getUserId()
                            );

            ApiResponse<List<Notification>> response =
                    new ApiResponse<>(
                            true,
                            "Notifications fetched successfully",
                            notifications
                    );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.OK
            );

        } catch (Exception e) {

            ApiResponse<List<Notification>> response =
                    new ApiResponse<>(
                            false,
                            e.getMessage(),
                            null
                    );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.BAD_REQUEST
            );
        }
    }


    // =========================
    // MARK NOTIFICATION AS READ
    // =========================

    public ResponseEntity<ApiResponse<Notification>> markAsRead(
            Long notificationId,
            Authentication authentication) {

        try {

            String email = authentication.getName();

            User user = userRepository
                    .findByEmailIgnoreCase(email)
                    .orElseThrow(() ->
                            new RuntimeException("User not found")
                    );

            Notification notification =
                    notificationRepository
                            .findById(notificationId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Notification not found"
                                    )
                            );

            // User can mark only their own notification
            if (!notification.getUserId()
                    .equals(user.getUserId())) {

                ApiResponse<Notification> response =
                        new ApiResponse<>(
                                false,
                                "You are not allowed to access this notification",
                                null
                        );

                return new ResponseEntity<>(
                        response,
                        HttpStatus.FORBIDDEN
                );
            }

            notification.setReadStatus(true);

            Notification updatedNotification =
                    notificationRepository.save(notification);

            ApiResponse<Notification> response =
                    new ApiResponse<>(
                            true,
                            "Notification marked as read",
                            updatedNotification
                    );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.OK
            );

        } catch (Exception e) {

            ApiResponse<Notification> response =
                    new ApiResponse<>(
                            false,
                            e.getMessage(),
                            null
                    );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}