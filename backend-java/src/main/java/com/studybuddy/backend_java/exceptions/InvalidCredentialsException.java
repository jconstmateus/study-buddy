package com.studybuddy.backend_java.exceptions;

// Exception 401
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}