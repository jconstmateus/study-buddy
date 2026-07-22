package com.studybuddy.backend_java.service;

import com.studybuddy.backend_java.model.ChangeResponse;
import com.studybuddy.backend_java.model.User;
import com.studybuddy.backend_java.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Constructor
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // Register new User with encode method
    public User register(User user) {

        // Verify required fields are not empty
        if (user.getName() == null || user.getName().isBlank()) {
            throw new RuntimeException("Name is required");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new RuntimeException("Email is required");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new RuntimeException("Password is required");
        }

        // Verify if email already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already registered");
        }

        // Encoding password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);   // Use of save from JPARepository
    }

    // Login of already existing user
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email);   // Use of findByEmail extended in UserRepository

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return jwtService.generateToken(email);
    }

    // Verify password to changing email or password
    public Boolean verify(String email, String password) {
        User user = userRepository.findByEmail(email);   // Use of findByEmail extended in UserRepository

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return true;
    }

    // Change password with encoding method
    public ChangeResponse changePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user); // Save in DB

        // Create response
        return new ChangeResponse("", "Password changed successfully");
    }

    // Change email and generate new token
    public ChangeResponse changeEmail(String email, String newEmail) {

        if (userRepository.findByEmail(newEmail) != null) {
            throw new RuntimeException("Email already registered");
        }

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        user.setEmail(newEmail);
        userRepository.save(user); // Save in DB

        // Create response
        return new ChangeResponse(jwtService.generateToken(newEmail), "Email changed successfully");
    }
}