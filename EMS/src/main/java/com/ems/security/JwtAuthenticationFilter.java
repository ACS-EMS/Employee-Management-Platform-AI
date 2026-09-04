package com.ems.security;

import com.ems.service.CustomUserDetailsService;
import com.ems.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader =
                request.getHeader("Authorization");

        System.out.println("Authorization Header: " + authHeader);

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            System.out.println("No Bearer token found");

            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        String email;

        try {

            email = jwtService.extractEmail(token);

            System.out.println(
                    "Email extracted from JWT: " + email
            );

        } catch (Exception e) {

            System.out.println(
                    "JWT ERROR: " + e.getMessage()
            );

            e.printStackTrace();

            filterChain.doFilter(request, response);
            return;
        }

        if (email != null &&
                SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null) {

            UserDetails userDetails =
                    userDetailsService
                            .loadUserByUsername(email);

            System.out.println(
                    "User loaded: " +
                            userDetails.getUsername()
            );

            boolean valid =
                    jwtService.validateToken(
                            token,
                            userDetails.getUsername()
                    );

            System.out.println(
                    "Token valid: " + valid
            );

            if (valid) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);

                System.out.println(
                        "AUTHENTICATED: " +
                                authentication.isAuthenticated()
                );

                System.out.println(
                        "Authorities: " +
                                authentication.getAuthorities()
                );
            }
        }

        filterChain.doFilter(request, response);
    }
}