package com.ems.service;

import com.ems.common.ApiResponse;
import com.ems.dto.LoginDto;
import com.ems.entity.User;
import com.ems.exception.InvalidCredentialsException;
import com.ems.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ApiResponse<User> login(LoginDto loginDto) {

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

        System.out.println("Password matches: " + passwordMatches);

        if (!passwordMatches) {
            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }



        return new ApiResponse<>(
                true,
                "Login successful",
                user
        );
    }
}