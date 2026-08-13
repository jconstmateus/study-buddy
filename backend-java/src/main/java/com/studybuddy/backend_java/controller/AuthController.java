package com.studybuddy.backend_java.controller;

import com.studybuddy.backend_java.dto.EmailOrPasswordChangeResponse;
import com.studybuddy.backend_java.dto.EmailChangeRequest;
import com.studybuddy.backend_java.dto.LoginRequest;
import com.studybuddy.backend_java.dto.PassChangeRequest;
import com.studybuddy.backend_java.dto.RegisterRequest;
import com.studybuddy.backend_java.dto.UserResponse;
import com.studybuddy.backend_java.model.*;
import com.studybuddy.backend_java.service.AuthService;
import com.studybuddy.backend_java.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    // Create new object User, with info from body
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

            User novoUser = authService.register(request);
            return ResponseEntity.ok(new UserResponse(novoUser));
    }

    // Find an already existing User, with info from small class LoginRequest
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        String token = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(token);

    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        return ResponseEntity.ok(new UserResponse(user));
    }

    @PutMapping("/me/change-password")
    public ResponseEntity<?> setNewPassword(Authentication authentication, @RequestBody PassChangeRequest change) {

        // Verify password first
        authService.verify(authentication.getName(), change.getPassword());
        // Change password
        EmailOrPasswordChangeResponse changeResponse = authService.changePassword(authentication.getName(), change.getNewPassword());
        return ResponseEntity.ok(changeResponse);
    }

    @PutMapping("/me/change-email")
    public ResponseEntity<?> setNewEmail(Authentication authentication, @RequestBody EmailChangeRequest change) {

        // Verify password first
        authService.verify(authentication.getName(), change.getPassword());
        // Change email
        EmailOrPasswordChangeResponse changeResponse = authService.changeEmail(authentication.getName(), change.getNewEmail());
        return ResponseEntity.ok(changeResponse);

    }
}


