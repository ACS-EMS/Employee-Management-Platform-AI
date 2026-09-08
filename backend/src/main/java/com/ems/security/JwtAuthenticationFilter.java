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
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

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

        // Get Authorization header
        String authHeader =
                request.getHeader("Authorization");

        System.out.println(
                "Authorization Header: " + authHeader
        );


        // No token
        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }


        // Remove "Bearer "
        String token =
                authHeader.substring(7);

        String email;


        try {

            email =
                    jwtService.extractEmail(token);

            System.out.println(
                    "JWT Email: " + email
            );

        } catch (Exception e) {

            System.out.println(
                    "Invalid JWT: "
                            + e.getMessage()
            );

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }


        // Authenticate only if authentication
        // is not already available
        if (email != null &&
                SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null) {

            UserDetails userDetails =
                    userDetailsService
                            .loadUserByUsername(email);


            boolean valid =
                    jwtService.validateToken(
                            token,
                            userDetails.getUsername()
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
                        "Authenticated User: "
                                + userDetails.getUsername()
                );

                System.out.println(
                        "Authorities: "
                                + userDetails.getAuthorities()
                );
            }
        }


        filterChain.doFilter(
                request,
                response
        );
    }
}