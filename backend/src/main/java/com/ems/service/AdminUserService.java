package com.ems.service;

import com.ems.common.ApiResponse;
import com.ems.dto.AdminUserDto;
import com.ems.entity.User;
import com.ems.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;

    public AdminUserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuditLogService auditLogService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditLogService = auditLogService;
    }

    // ============================================
    // GET USERS + SEARCH + FILTER
    // ============================================

    public ResponseEntity<ApiResponse<List<AdminUserDto>>> getUsers(
            String search,
            String role,
            Boolean active
    ) {

        try {

            List<User> users =
                    userRepository.findAll();

            List<AdminUserDto> result =
                    users.stream()

                            .filter(user -> {

                                if (
                                        search == null ||
                                                search.trim().isEmpty()
                                ) {
                                    return true;
                                }

                                String searchValue =
                                        search.trim().toLowerCase();

                                boolean nameMatches =
                                        user.getUserName() != null &&
                                                user.getUserName()
                                                        .toLowerCase()
                                                        .contains(searchValue);

                                boolean emailMatches =
                                        user.getEmail() != null &&
                                                user.getEmail()
                                                        .toLowerCase()
                                                        .contains(searchValue);

                                return nameMatches || emailMatches;
                            })

                            .filter(user -> {

                                if (
                                        role == null ||
                                                role.trim().isEmpty()
                                ) {
                                    return true;
                                }

                                return user.getRole() != null &&
                                        user.getRole()
                                                .equalsIgnoreCase(role);
                            })

                            .filter(user -> {

                                if (active == null) {
                                    return true;
                                }

                                return active.equals(
                                        user.getActive()
                                );
                            })

                            .map(this::convertToDto)
                            .toList();

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "Users fetched successfully",
                            result
                    )
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Failed to fetch users",
                                    null
                            )
                    );
        }
    }

    // ============================================
    // GET USER BY ID
    // ============================================

    public ResponseEntity<ApiResponse<AdminUserDto>> getUserById(
            Long userId
    ) {

        try {

            User user = userRepository
                    .findById(userId)
                    .orElse(null);

            if (user == null) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(
                                new ApiResponse<>(
                                        false,
                                        "User not found",
                                        null
                                )
                        );
            }

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "User fetched successfully",
                            convertToDto(user)
                    )
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Failed to fetch user",
                                    null
                            )
                    );
        }
    }

    // ============================================
    // CREATE USER
    // ============================================

    public ResponseEntity<ApiResponse<AdminUserDto>> createUser(
            AdminUserDto dto
    ) {

        try {

            if (
                    dto.getUserName() == null ||
                            dto.getUserName().trim().isEmpty()
            ) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                new ApiResponse<>(
                                        false,
                                        "User name is required",
                                        null
                                )
                        );
            }

            if (
                    dto.getEmail() == null ||
                            dto.getEmail().trim().isEmpty()
            ) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                new ApiResponse<>(
                                        false,
                                        "Email is required",
                                        null
                                )
                        );
            }

            if (
                    dto.getPassword() == null ||
                            dto.getPassword().trim().isEmpty()
            ) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                new ApiResponse<>(
                                        false,
                                        "Password is required",
                                        null
                                )
                        );
            }

            if (
                    userRepository
                            .findByEmailIgnoreCase(dto.getEmail())
                            .isPresent()
            ) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                new ApiResponse<>(
                                        false,
                                        "Email already registered",
                                        null
                                )
                        );
            }

            User user = new User();

            user.setUserName(
                    dto.getUserName().trim()
            );

            user.setEmail(
                    dto.getEmail()
                            .trim()
                            .toLowerCase()
            );

            user.setPassword(
                    passwordEncoder.encode(
                            dto.getPassword()
                    )
            );

            user.setRole(
                    dto.getRole()
            );

            user.setDepartment(
                    dto.getDepartment()
            );

            user.setActive(
                    dto.getActive() != null
                            ? dto.getActive()
                            : true
            );

            User savedUser =
                    userRepository.save(user);

            createAuditLogSafely(
                    "CREATE",
                    "Created user: " +
                            savedUser.getEmail()
            );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(
                            new ApiResponse<>(
                                    true,
                                    "User created successfully",
                                    convertToDto(savedUser)
                            )
                    );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Failed to create user",
                                    null
                            )
                    );
        }
    }

    // ============================================
    // UPDATE USER
    // ============================================

    public ResponseEntity<ApiResponse<AdminUserDto>> updateUser(
            Long userId,
            AdminUserDto dto
    ) {

        try {

            User user = userRepository
                    .findById(userId)
                    .orElse(null);

            if (user == null) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(
                                new ApiResponse<>(
                                        false,
                                        "User not found",
                                        null
                                )
                        );
            }

            if (
                    dto.getEmail() != null &&
                            !dto.getEmail().trim().isEmpty()
            ) {

                User existingUser =
                        userRepository
                                .findByEmailIgnoreCase(
                                        dto.getEmail()
                                )
                                .orElse(null);

                if (
                        existingUser != null &&
                                !existingUser
                                        .getUserId()
                                        .equals(userId)
                ) {

                    return ResponseEntity
                            .badRequest()
                            .body(
                                    new ApiResponse<>(
                                            false,
                                            "Email already registered",
                                            null
                                    )
                            );
                }
            }

            if (
                    dto.getUserName() != null &&
                            !dto.getUserName().trim().isEmpty()
            ) {

                user.setUserName(
                        dto.getUserName().trim()
                );
            }

            if (
                    dto.getEmail() != null &&
                            !dto.getEmail().trim().isEmpty()
            ) {

                user.setEmail(
                        dto.getEmail()
                                .trim()
                                .toLowerCase()
                );
            }

            if (
                    dto.getRole() != null &&
                            !dto.getRole().trim().isEmpty()
            ) {

                user.setRole(
                        dto.getRole()
                );
            }

            if (dto.getDepartment() != null) {
                user.setDepartment(
                        dto.getDepartment()
                );
            }

            if (dto.getActive() != null) {

                user.setActive(
                        dto.getActive()
                );
            }

            if (
                    dto.getPassword() != null &&
                            !dto.getPassword().trim().isEmpty()
            ) {

                user.setPassword(
                        passwordEncoder.encode(
                                dto.getPassword()
                        )
                );
            }

            User updatedUser =
                    userRepository.save(user);

            createAuditLogSafely(
                    "UPDATE",
                    "Updated user: " +
                            updatedUser.getEmail()
            );

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "User updated successfully",
                            convertToDto(updatedUser)
                    )
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Failed to update user",
                                    null
                            )
                    );
        }
    }

    // ============================================
    // ACTIVATE / DEACTIVATE USER
    // ============================================

    public ResponseEntity<ApiResponse<AdminUserDto>> updateStatus(
            Long userId,
            Boolean active
    ) {

        try {

            User user = userRepository
                    .findById(userId)
                    .orElse(null);

            if (user == null) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(
                                new ApiResponse<>(
                                        false,
                                        "User not found",
                                        null
                                )
                        );
            }

            user.setActive(active);

            User updatedUser =
                    userRepository.save(user);

            createAuditLogSafely(
                    "STATUS",
                    active
                            ? "Activated user: " +
                            updatedUser.getEmail()
                            : "Deactivated user: " +
                            updatedUser.getEmail()
            );

            String message =
                    active
                            ? "User activated successfully"
                            : "User deactivated successfully";

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            message,
                            convertToDto(updatedUser)
                    )
            );

        }catch (Exception e) {

<<<<<<< Updated upstream
            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Failed to update user status",
                                    null
                            )
                    );
        }
=======
    e.printStackTrace();

    return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                    new ApiResponse<>(
                            false,
                            "Failed to update user status: " + e.getMessage(),
                            null
                    )
            );
}
>>>>>>> Stashed changes
    }

    // ============================================
    // DELETE USER
    // ============================================

    public ResponseEntity<ApiResponse<Void>> deleteUser(
            Long userId
    ) {

        try {

            User user =
                    userRepository
                            .findById(userId)
                            .orElse(null);

            if (user == null) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(
                                new ApiResponse<>(
                                        false,
                                        "User not found",
                                        null
                                )
                        );
            }

            String deletedUserEmail =
                    user.getEmail();

            userRepository.delete(user);

            createAuditLogSafely(
                    "DELETE",
                    "Deleted user: " +
                            deletedUserEmail
            );

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "User deleted successfully",
                            null
                    )
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Failed to delete user",
                                    null
                            )
                    );
        }
    }

    // ============================================
    // SAVE AUDIT LOG SAFELY
    // ============================================

    private void createAuditLogSafely(
            String action,
            String details
    ) {

        try {

            auditLogService.createAuditLog(
                    "USERS",
                    action,
                    details,
                    getCurrentUser()
            );

        } catch (Exception e) {

            System.out.println(
                    "Audit log save failed: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    // ============================================
    // CURRENT LOGGED-IN USER
    // ============================================

    private String getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (
                authentication == null ||
                        !authentication.isAuthenticated()
        ) {
            return "SYSTEM";
        }

        return authentication.getName();
    }

    // ============================================
    // ENTITY -> DTO
    // ============================================

    private AdminUserDto convertToDto(
            User user
    ) {

        AdminUserDto dto =
                new AdminUserDto();

        dto.setUserId(
                user.getUserId()
        );

        dto.setUserName(
                user.getUserName()
        );

        dto.setDepartment(
                user.getDepartment()
        );

        dto.setEmail(
                user.getEmail()
        );

        dto.setRole(
                user.getRole()
        );

        dto.setActive(
                user.getActive()
        );

        dto.setPassword(null);

        return dto;
    }
}