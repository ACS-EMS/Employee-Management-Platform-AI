package com.ems.service;

import com.ems.common.ApiResponse;
import com.ems.dto.AdminUserDto;
import com.ems.entity.User;
import com.ems.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

            List<User> users = userRepository.findAll();

            List<AdminUserDto> result = users
                    .stream()

                    // SEARCH
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

                    // ROLE FILTER
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

                    // ACTIVE / INACTIVE FILTER
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

            // Check duplicate email
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

            // BCrypt password
            user.setPassword(
                    passwordEncoder.encode(
                            dto.getPassword()
                    )
            );

            user.setRole(
                    dto.getRole()
            );

            user.setActive(
                    dto.getActive() != null
                            ? dto.getActive()
                            : true
            );

            User savedUser =
                    userRepository.save(user);

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

            // Check if email belongs to another user
            if (dto.getEmail() != null) {

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

            if (dto.getActive() != null) {

                user.setActive(
                        dto.getActive()
                );
            }

            // Only update password if admin entered one
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

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "User updated successfully",
                            convertToDto(updatedUser)
                    )
            );

        } catch (Exception e) {

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
    }

    // ============================================
    // DELETE USER
    // ============================================

    public ResponseEntity<ApiResponse<Void>> deleteUser(
            Long userId
    ) {

        try {

            if (
                    !userRepository.existsById(userId)
            ) {

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

            userRepository.deleteById(userId);

            return ResponseEntity.ok(
                    new ApiResponse<>(
                            true,
                            "User deleted successfully",
                            null
                    )
            );

        } catch (Exception e) {

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

        dto.setEmail(
                user.getEmail()
        );

        dto.setRole(
                user.getRole()
        );

        dto.setActive(
                user.getActive()
        );

        // IMPORTANT:
        // Never return password
        dto.setPassword(null);

        return dto;
    }
}