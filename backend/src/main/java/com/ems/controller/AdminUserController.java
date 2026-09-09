package com.ems.controller;

import com.ems.common.ApiResponse;
import com.ems.dto.AdminUserDto;
import com.ems.service.AdminUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    // ==========================================
    // GET ALL USERS
    // Search + Role Filter + Status Filter
    // ==========================================
    @GetMapping
    public ResponseEntity<ApiResponse<List<AdminUserDto>>> getUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean active
    ) {

        return adminUserService.getUsers(
                search,
                role,
                active
        );
    }

    // ==========================================
    // GET USER BY ID
    // ==========================================
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<AdminUserDto>> getUserById(
            @PathVariable Long userId
    ) {

        return adminUserService.getUserById(userId);
    }

    // ==========================================
    // CREATE USER
    // ==========================================
    @PostMapping
    public ResponseEntity<ApiResponse<AdminUserDto>> createUser(
            @RequestBody AdminUserDto adminUserDto
    ) {

        return adminUserService.createUser(adminUserDto);
    }

    // ==========================================
    // UPDATE USER
    // ==========================================
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<AdminUserDto>> updateUser(
            @PathVariable Long userId,
            @RequestBody AdminUserDto adminUserDto
    ) {

        return adminUserService.updateUser(
                userId,
                adminUserDto
        );
    }

    // ==========================================
    // ACTIVATE / DEACTIVATE USER
    // ==========================================
    @PatchMapping("/{userId}/status")
    public ResponseEntity<ApiResponse<AdminUserDto>> updateUserStatus(
            @PathVariable Long userId,
            @RequestParam Boolean active
    ) {

        return adminUserService.updateStatus(
                userId,
                active
        );
    }

    // ==========================================
    // DELETE USER
    // ==========================================
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable Long userId
    ) {

        return adminUserService.deleteUser(userId);
    }
}