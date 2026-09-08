package com.ems.service;

import com.ems.common.ApiResponse;
import com.ems.dto.AuthResponseDto;
import com.ems.dto.LoginDto;
import com.ems.dto.SignupDto;
import com.ems.entity.User;
import com.ems.exception.InvalidCredentialsException;
import com.ems.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;


    public ResponseEntity<ApiResponse<AuthResponseDto>> login(
            LoginDto loginDto) {

        try {

            User user = userRepository
                    .findByEmailIgnoreCase(loginDto.getEmail())
                    .orElseThrow(() ->
                            new InvalidCredentialsException(
                                    "Invalid email or password"
                            )
                    );

            boolean passwordMatches =
                    passwordEncoder.matches(
                            loginDto.getPassword(),
                            user.getPassword()
                    );

            if (!passwordMatches) {
                throw new InvalidCredentialsException(
                        "Invalid email or password"
                );
            }

            String token =
                    jwtService.generateToken(
                            user.getEmail(),
                            user.getRole()
                    );

            AuthResponseDto authResponse =
                    new AuthResponseDto(
                            user.getUserId(),
                            user.getUserName(),
                            user.getEmail(),
                            user.getRole(),
                            token
                    );

            ApiResponse<AuthResponseDto> response =
                    new ApiResponse<>(
                            true,
                            "Login successful",
                            authResponse
                    );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.OK
            );

        } catch (InvalidCredentialsException e) {

            ApiResponse<AuthResponseDto> response =
                    new ApiResponse<>(
                            false,
                            e.getMessage(),
                            null
                    );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.UNAUTHORIZED
            );
        }
    }


    public ResponseEntity<ApiResponse<User>> signup(
            SignupDto signupDto) {

        try {

            boolean emailExists =
                    userRepository
                            .findByEmailIgnoreCase(
                                    signupDto.getEmail()
                            )
                            .isPresent();

            if (emailExists) {

                ApiResponse<User> response =
                        new ApiResponse<>(
                                false,
                                "Email already registered",
                                null
                        );

                return new ResponseEntity<>(
                        response,
                        HttpStatus.CONFLICT
                );
            }

            User user = new User();

            user.setUserName(
                    signupDto.getUserName()
            );

            user.setEmail(
                    signupDto.getEmail()
            );

            user.setPassword(
                    passwordEncoder.encode(
                            signupDto.getPassword()
                    )
            );

            user.setRole(
                    signupDto.getRole()
            );

            user.setActive(true);

            User savedUser =
                    userRepository.save(user);

            ApiResponse<User> response =
                    new ApiResponse<>(
                            true,
                            "User registered successfully",
                            savedUser
                    );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.CREATED
            );

        } catch (Exception e) {

            ApiResponse<User> response =
                    new ApiResponse<>(
                            false,
                            "Registration failed",
                            null
                    );

            return new ResponseEntity<>(
                    response,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}