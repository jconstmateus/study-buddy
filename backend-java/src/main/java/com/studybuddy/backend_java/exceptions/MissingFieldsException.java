package com.studybuddy.backend_java.exceptions;

// Exception 400
public class MissingFieldsException extends RuntimeException {
    public MissingFieldsException(String message) {
        super(message);
    }
}