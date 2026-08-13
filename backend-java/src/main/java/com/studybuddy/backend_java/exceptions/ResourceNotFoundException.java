package com.studybuddy.backend_java.exceptions;

// Exception 404 - generic, for any entity (Course, Event, Document, ...) not found by id
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
