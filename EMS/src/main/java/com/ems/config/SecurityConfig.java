package com.ems.config;

import com.ems.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

                // =========================
                // CSRF
                // =========================
                .csrf(csrf ->
                        csrf.disable()
                )


                // =========================
                // SESSION
                // =========================
                // JWT is stateless,
                // so Spring should not create sessions
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )


                // =========================
                // AUTHORIZATION
                // =========================
                .authorizeHttpRequests(auth -> auth


                        // =========================
                        // AUTH APIs
                        // =========================

                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/signup"
                        ).permitAll()


                        // =========================
                        // JOB APIs
                        // =========================

                        // Logged-in users can view jobs
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/jobs",
                                "/api/jobs/**"
                        ).authenticated()


                        // Employer/Admin can create jobs
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/jobs"
                        ).hasAnyRole(
                                "EMPLOYER",
                                "ADMIN"
                        )


                        // Employer/Admin can update jobs
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/jobs/**"
                        ).hasAnyRole(
                                "EMPLOYER",
                                "ADMIN"
                        )


                        // Employer/Admin can delete jobs
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/jobs/**"
                        ).hasAnyRole(
                                "EMPLOYER",
                                "ADMIN"
                        )


                        // =========================
                        // APPLICATION APIs
                        // =========================

                        // Candidate applies for job
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/applications/apply/**"
                        ).hasRole(
                                "CANDIDATE"
                        )


                        // Candidate views own applications
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/applications/my"
                        ).hasRole(
                                "CANDIDATE"
                        )


                        // Candidate withdraws own application
                        // Keep this BEFORE general PUT application rule
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/applications/*/withdraw"
                        ).hasRole(
                                "CANDIDATE"
                        )


                        // Employer/Admin views applicants
                        // for a particular job
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/applications/job/**"
                        ).hasAnyRole(
                                "EMPLOYER",
                                "ADMIN"
                        )


                        // Employer/Admin updates
                        // application status
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/applications/**"
                        ).hasAnyRole(
                                "EMPLOYER",
                                "ADMIN"
                        )


                        // =========================
                        // INTERVIEW APIs
                        // =========================

                        // Employer/Admin schedules interview
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/interviews/schedule"
                        ).hasAnyRole(
                                "EMPLOYER",
                                "ADMIN"
                        )


                        // Candidate views own interviews
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/interviews/my"
                        ).hasRole(
                                "CANDIDATE"
                        )


                        // Employer/Admin views
                        // interviews for a job
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/interviews/job/**"
                        ).hasAnyRole(
                                "EMPLOYER",
                                "ADMIN"
                        )


                        // Employer/Admin reschedules interview
                        // or updates interview status
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/interviews/**"
                        ).hasAnyRole(
                                "EMPLOYER",
                                "ADMIN"
                        )


                        // =========================
                        // OTHER APIs
                        // =========================

                        .anyRequest()
                        .authenticated()
                )


                // =========================
                // JWT FILTER
                // =========================

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );


        return http.build();
    }


    // =========================
    // PASSWORD ENCODER
    // =========================




    // =========================
    // AUTHENTICATION MANAGER
    // =========================

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration
                .getAuthenticationManager();
    }
}