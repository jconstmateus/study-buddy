package com.studybuddy.backend_java.exceptions;

// Exception 409
public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}