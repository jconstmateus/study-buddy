package com.studybuddy.backend_java.model;

// Small class to configure a padronized response when changing email/password
public class ChangeResponse {

    private String token;
    private String message;

    public ChangeResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    // GETTERS
    public String getToken() { return token; }
    public String getMessage() { return message;}
}
