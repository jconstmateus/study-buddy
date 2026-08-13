package com.studybuddy.backend_java.exceptions;

// Exception 404
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}