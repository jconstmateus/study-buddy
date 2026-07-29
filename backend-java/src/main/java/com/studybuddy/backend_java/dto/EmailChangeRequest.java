package com.studybuddy.backend_java.dto;

public class EmailChangeRequest {

    // Get only this info from body when logging
    private String password;
    private String newEmail;

    // Constructor
    public EmailChangeRequest() {}

    // Getters and Setters
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNewEmail() { return newEmail; }
    public void setNewEmail(String newEmail) { this.newEmail = newEmail; }


}
