package com.studybuddy.backend_java.exceptions;

// Exception 403
public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException(String message) {
        super(message);
    }
}