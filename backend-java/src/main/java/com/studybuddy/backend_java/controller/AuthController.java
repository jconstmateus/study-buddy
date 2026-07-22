package com.studybuddy.backend_java.controller;

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
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User novoUser = authService.register(user);
            return ResponseEntity.ok(novoUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    // Find an already existing User, with info from small class LoginRequest
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = authService.login(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(token);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        return ResponseEntity.ok(user);
    }

    @PutMapping("/me/change-password")
    public ResponseEntity<?> setNewPassword(Authentication authentication, @RequestBody PassChange change) {

        // Verify password first
        try {
            authService.verify(authentication.getName(), change.getPassword());

        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }

        try {
            ChangeResponse changeResponse = authService.changePassword(authentication.getName(), change.getNewPassword());
            return ResponseEntity.ok(changeResponse);

        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @PutMapping("/me/change-email")
    public ResponseEntity<?> setNewEmail(Authentication authentication, @RequestBody EmailChange change) {

        // Verify password first
        try {
            authService.verify(authentication.getName(), change.getPassword());

        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }

        try {
            ChangeResponse changeResponse = authService.changeEmail(authentication.getName(), change.getNewEmail());
            return ResponseEntity.ok(changeResponse);

        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }
}


