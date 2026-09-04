package com.ems.controller;

import com.ems.common.ApiResponse;
import com.ems.dto.LoginDto;
import com.ems.dto.SignupDto;
import com.ems.entity.User;
import com.ems.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<User>> login(
            @RequestBody LoginDto loginDto) {

        return authService.login(loginDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<User>> signup(
            @RequestBody SignupDto signupDto) {

        return authService.signup(signupDto);
    }
}