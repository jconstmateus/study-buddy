package com.studybuddy.backend_java.dto;

// Small class to configure a padronized response when changing email/password
public class EmailOrPasswordChangeResponse {

    private String token;
    private String message;

    public EmailOrPasswordChangeResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    // GETTERS
    public String getToken() { return token; }
    public String getMessage() { return message;}
}
