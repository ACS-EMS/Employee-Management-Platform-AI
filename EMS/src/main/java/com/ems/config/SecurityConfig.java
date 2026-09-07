package com.ems.config;

import com.ems.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http

                // =========================
                // CSRF
                // =========================
                .csrf(csrf -> csrf.disable())

                // =========================
                // SESSION
                // =========================
                // JWT authentication is stateless
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

                        // Any authenticated user can view jobs
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/jobs",
                                "/api/jobs/**"
                        ).authenticated()


                        // Employer / HR / Hiring Manager /
                        // Recruiter / Super Admin can create jobs
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/jobs"
                        ).hasAnyRole(
                                "EMPLOYER",
                                "HR",
                                "HIRING_MANAGER",
                                "RECRUITER",
                                "SUPER_ADMIN"
                        )


                        // Employer / HR / Hiring Manager /
                        // Recruiter / Super Admin can update jobs
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/jobs/**"
                        ).hasAnyRole(
                                "EMPLOYER",
                                "HR",
                                "HIRING_MANAGER",
                                "RECRUITER",
                                "SUPER_ADMIN"
                        )


                        // Employer / Super Admin can delete jobs
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/jobs/**"
                        ).hasAnyRole(
                                "EMPLOYER",
                                "SUPER_ADMIN"
                        )


                        // =========================
                        // APPLICATION APIs
                        // =========================

                        // Candidate applies for a job
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/applications/apply/**"
                        ).hasRole(
                                "CANDIDATE"
                        )


                        // Candidate views their applications
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/applications/my"
                        ).hasRole(
                                "CANDIDATE"
                        )


                        // Candidate withdraws their application
                        // Must come before general PUT rule
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/applications/*/withdraw"
                        ).hasRole(
                                "CANDIDATE"
                        )


                        // HR / Employer / Hiring Manager /
                        // Recruiter / Super Admin views job applicants
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/applications/job/**"
                        ).hasAnyRole(
                                "EMPLOYER",
                                "HR",
                                "HIRING_MANAGER",
                                "RECRUITER",
                                "SUPER_ADMIN"
                        )


                        // HR / Employer / Hiring Manager /
                        // Recruiter / Super Admin updates application status
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/applications/**"
                        ).hasAnyRole(
                                "EMPLOYER",
                                "HR",
                                "HIRING_MANAGER",
                                "RECRUITER",
                                "SUPER_ADMIN"
                        )


                        // =========================
                        // INTERVIEW APIs
                        // =========================

                        // HR / Hiring Manager /
                        // Recruiter / Super Admin schedules interview
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/interviews/schedule"
                        ).hasAnyRole(
                                "HR",
                                "HIRING_MANAGER",
                                "RECRUITER",
                                "SUPER_ADMIN"
                        )


                        // Candidate views own interviews
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/interviews/my"
                        ).hasRole(
                                "CANDIDATE"
                        )


                        // HR / Hiring Manager /
                        // Recruiter / Super Admin views interviews for a job
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/interviews/job/**"
                        ).hasAnyRole(
                                "HR",
                                "HIRING_MANAGER",
                                "RECRUITER",
                                "SUPER_ADMIN"
                        )


                        // Interviewer views assigned interviews
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/interviews/interviewer/**"
                        ).hasAnyRole(
                                "INTERVIEWER",
                                "HR",
                                "HIRING_MANAGER",
                                "RECRUITER",
                                "SUPER_ADMIN"
                        )


                        // HR / Hiring Manager /
                        // Recruiter / Super Admin reschedules
                        // or updates interview
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/interviews/**"
                        ).hasAnyRole(
                                "HR",
                                "HIRING_MANAGER",
                                "RECRUITER",
                                "SUPER_ADMIN"
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
    // AUTHENTICATION MANAGER
    // =========================

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }
}