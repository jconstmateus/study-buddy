package com.studybuddy.backend_java.dto;

public class RegisterRequest {

    // Only what's needed to create a new User — no id, no createdAt
    private String name;
    private String email;
    private String password;

    public RegisterRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

}
