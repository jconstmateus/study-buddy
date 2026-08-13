package com.studybuddy.backend_java.service;

import com.studybuddy.backend_java.exceptions.EmailAlreadyExistsException;
import com.studybuddy.backend_java.exceptions.InvalidCredentialsException;
import com.studybuddy.backend_java.exceptions.MissingFieldsException;
import com.studybuddy.backend_java.exceptions.UserNotFoundException;
import com.studybuddy.backend_java.dto.EmailOrPasswordChangeResponse;
import com.studybuddy.backend_java.dto.RegisterRequest;
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
    public User register(RegisterRequest request) {

        // Verify required fields are not empty
        if (request.getName() == null || request.getName().isBlank()) {
            throw new MissingFieldsException("Name is required");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new MissingFieldsException("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new MissingFieldsException("Password is required");
        }

        // Verify if email already exists
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        // Always build a fresh User — id stays null, so save() can only ever INSERT, never overwrite an existing row
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);   // Use of save from JPARepository
    }

    // Login of already existing user
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email);   // Use of findByEmail extended in UserRepository

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return jwtService.generateToken(email);
    }

    // Verify password to changing email or password
    public Boolean verify(String email, String password) {
        User user = userRepository.findByEmail(email);   // Use of findByEmail extended in UserRepository

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }

        return true;
    }

    // Change password with encoding method
    public EmailOrPasswordChangeResponse changePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user); // Save in DB

        // Create response
        return new EmailOrPasswordChangeResponse("", "Password changed successfully");
    }

    // Change email and generate new token
    public EmailOrPasswordChangeResponse changeEmail(String email, String newEmail) {

        if (userRepository.findByEmail(newEmail) != null) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        user.setEmail(newEmail);
        userRepository.save(user); // Save in DB

        // Create response
        return new EmailOrPasswordChangeResponse(jwtService.generateToken(newEmail), "Email changed successfully");
    }
}