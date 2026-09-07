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
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

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

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/jobs",
                                "/api/jobs/**"
                        ).authenticated()

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

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/applications/apply/**"
                        ).hasRole("CANDIDATE")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/applications/my"
                        ).hasRole("CANDIDATE")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/applications/*/withdraw"
                        ).hasRole("CANDIDATE")

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

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/interviews/schedule"
                        ).hasAnyRole(
                                "HR",
                                "HIRING_MANAGER",
                                "RECRUITER",
                                "SUPER_ADMIN"
                        )

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/interviews/my"
                        ).hasRole("CANDIDATE")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/interviews/job/**"
                        ).hasAnyRole(
                                "HR",
                                "HIRING_MANAGER",
                                "RECRUITER",
                                "SUPER_ADMIN"
                        )

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
                        // INTERVIEW FEEDBACK APIs
                        // =========================

                        // Interviewer submits feedback
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/interview-feedback"
                        ).hasRole(
                                "INTERVIEWER"
                        )

                        // HR / Hiring Manager /
                        // Recruiter / Super Admin view feedback
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/interview-feedback/**"
                        ).hasAnyRole(
                                "HR",
                                "HIRING_MANAGER",
                                "RECRUITER",
                                "SUPER_ADMIN"
                        )


                        // =========================
                        // OTHER APIs
                        // =========================
// =========================
// NOTIFICATION APIs
// =========================

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/notifications/**"
                                ).authenticated()

                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/notifications/**"
                                ).authenticated()
                        .anyRequest()
                        .authenticated()
                )

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