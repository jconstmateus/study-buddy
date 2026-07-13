package com.studybuddy.backend_java.model;

public class LoginRequest {

    // Get only this info from body when logging
    private String email;
    private String password;

    // Constructor
    public LoginRequest() {}

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

}