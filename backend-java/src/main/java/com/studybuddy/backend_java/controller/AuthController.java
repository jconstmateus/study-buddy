package com.studybuddy.backend_java.controller;

import com.studybuddy.backend_java.model.LoginRequest;
import com.studybuddy.backend_java.model.User;
import com.studybuddy.backend_java.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Create new object User, with info from body
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        // Catch throw if user already exists by email
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
            User user = authService.login(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}