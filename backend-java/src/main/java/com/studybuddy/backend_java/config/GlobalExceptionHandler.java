package com.studybuddy.backend_java.config;

import com.studybuddy.backend_java.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Specific exceptions
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<?> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        return ResponseEntity.status(409).body(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<?> handleInvalidCredentialException(InvalidCredentialsException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }

    @ExceptionHandler(MissingFieldsException.class)
    public ResponseEntity<?> handleMissingFieldsException(MissingFieldsException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<?> handleNotAuthorizedException(NotAuthorizedException e) {
        return ResponseEntity.status(403).body(e.getMessage());
    }

    // Handle general exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        logger.error("Unhandled exception", e);
        return ResponseEntity.status(500).body("Internal error");
    }

}
