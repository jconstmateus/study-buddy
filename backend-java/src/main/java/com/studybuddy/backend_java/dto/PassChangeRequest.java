package com.studybuddy.backend_java.dto;

public class PassChangeRequest {

    // Get only this info from body when logging
    private String password;
    private String newPassword;

    // Constructor
    public PassChangeRequest() {}

    // Getters and Setters
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

}
