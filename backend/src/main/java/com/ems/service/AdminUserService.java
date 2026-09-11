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
    public ResponseEntity<ApiResponse<List<AdminUserDto>>> getUsers(
            String search,
            String role,
            Boolean active
    ) {

        try {

            List<User> users = userRepository.findAll();

            List<AdminUserDto> result = users.stream()

                    // Search by name or email
                    .filter(user -> {

                        if (search == null || search.trim().isEmpty()) {
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

                        if (role == null || role.trim().isEmpty()) {
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

                        return active.equals(user.getActive());
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
                                    "Failed to fetch users: " + e.getMessage(),
                                    null
                            )
                    );
        }
    }


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
                                    "Failed to fetch user: " + e.getMessage(),
                                    null
                            )
                    );
        }
    }

    // ============================================================
    // CREATE USER
    // ============================================================

    public ResponseEntity<ApiResponse<AdminUserDto>> createUser(
            AdminUserDto dto
    ) {

        try {

            // Validate username
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

            // Validate password
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

            // Validate role
            if (
                    dto.getRole() == null ||
                            dto.getRole().trim().isEmpty()
            ) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                new ApiResponse<>(
                                        false,
                                        "Role is required",
                                        null
                                )
                        );
            }

            // Check duplicate email
            if (
                    userRepository
                            .findByEmailIgnoreCase(dto.getEmail().trim())
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
                            dto.getPassword().trim()
                    )
            );

            user.setRole(
                    dto.getRole().trim()
            );

            user.setDepartment(
                    dto.getDepartment() != null
                            ? dto.getDepartment().trim()
                            : null
            );

            user.setActive(
                    dto.getActive() != null
                            ? dto.getActive()
                            : true
            );

            User savedUser =
                    userRepository.save(user);

            // Create audit log
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
                                    "Failed to create user: " + e.getMessage(),
                                    null
                            )
                    );
        }
    }

    // ============================================================
    // UPDATE USER
    // ============================================================

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

            // ----------------------------------------------------
            // CHECK EMAIL DUPLICATION
            // ----------------------------------------------------

            if (
                    dto.getEmail() != null &&
                            !dto.getEmail().trim().isEmpty()
            ) {

                User existingUser =
                        userRepository
                                .findByEmailIgnoreCase(
                                        dto.getEmail().trim()
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

            // ----------------------------------------------------
            // UPDATE USER NAME
            // ----------------------------------------------------

            if (
                    dto.getUserName() != null &&
                            !dto.getUserName().trim().isEmpty()
            ) {

                user.setUserName(
                        dto.getUserName().trim()
                );
            }

            // ----------------------------------------------------
            // UPDATE EMAIL
            // ----------------------------------------------------

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

            // ----------------------------------------------------
            // UPDATE ROLE
            // ----------------------------------------------------

            if (
                    dto.getRole() != null &&
                            !dto.getRole().trim().isEmpty()
            ) {

                user.setRole(
                        dto.getRole().trim()
                );
            }

            // ----------------------------------------------------
            // UPDATE DEPARTMENT
            // ----------------------------------------------------

            if (dto.getDepartment() != null) {

                String department =
                        dto.getDepartment().trim();

                user.setDepartment(
                        department.isEmpty()
                                ? null
                                : department
                );
            }

            // ----------------------------------------------------
            // UPDATE ACTIVE STATUS
            // ----------------------------------------------------

            if (dto.getActive() != null) {

                user.setActive(
                        dto.getActive()
                );
            }

            // ----------------------------------------------------
            // UPDATE PASSWORD ONLY IF PROVIDED
            // ----------------------------------------------------

            if (
                    dto.getPassword() != null &&
                            !dto.getPassword().trim().isEmpty()
            ) {

                user.setPassword(
                        passwordEncoder.encode(
                                dto.getPassword().trim()
                        )
                );
            }

            User updatedUser =
                    userRepository.save(user);

            // Create audit log
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
                                    "Failed to update user: " + e.getMessage(),
                                    null
                            )
                    );
        }
    }

    // ============================================================
    // ACTIVATE / DEACTIVATE USER
    // ============================================================

    public ResponseEntity<ApiResponse<AdminUserDto>> updateStatus(
            Long userId,
            Boolean active
    ) {

        try {

            if (active == null) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                new ApiResponse<>(
                                        false,
                                        "Active status is required",
                                        null
                                )
                        );
            }

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

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Failed to update user status: "
                                            + e.getMessage(),
                                    null
                            )
                    );
        }
    }

    // ============================================================
    // DELETE USER
    // ============================================================

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
                                    "Failed to delete user: "
                                            + e.getMessage(),
                                    null
                            )
                    );
        }
    }

    // ============================================================
    // SAVE AUDIT LOG SAFELY
    // ============================================================

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

            /*
             * Audit-log failure should NOT stop
             * user create/update/delete/status operations.
             */
            System.out.println(
                    "Audit log save failed: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    // ============================================================
    // CURRENT LOGGED-IN USER
    // ============================================================

    private String getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (
                authentication == null ||
                        !authentication.isAuthenticated() ||
                        "anonymousUser"
                                .equals(authentication.getPrincipal())
        ) {

            return "SYSTEM";
        }

        return authentication.getName();
    }

    // ============================================================
    // ENTITY -> DTO
    // ============================================================

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

        // Never send BCrypt password to frontend
        dto.setPassword(null);

        return dto;
    }
}