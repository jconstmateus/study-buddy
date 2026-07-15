package com.studybuddy.backend_java.config;

import com.studybuddy.backend_java.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


// Extend base interface for each request
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    // Service provided: verify token
    private final JwtService jwtService;

    // Constructor
    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    // Methods
    // Filter for each request HTTP
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Extract header from request
        String authHeader = request.getHeader("Authorization");

        // If there is no token, proceed without auth
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract substring from header, excluding "Bearer "
        String token = authHeader.substring(7);

        try {
            // Validate token and return email if suceed
            String email = jwtService.validateTokenAndGetEmail(token);

            // Create authorisation
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(email, null, null);
            // Store the authentication within the context
            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (Exception e) {
            // Invalid token, proceed
        }

        filterChain.doFilter(request, response);
    }
}