package com.studybuddy.backend_java.config;

import com.studybuddy.backend_java.exceptions.EmailAlreadyExistsException;
import com.studybuddy.backend_java.exceptions.InvalidCredentialsException;
import com.studybuddy.backend_java.exceptions.MissingFieldsException;
import com.studybuddy.backend_java.exceptions.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Specific exceptions
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<?> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        return ResponseEntity.status(409).body(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException e) {
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

    // Handle general exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.status(500).body("Internal error");
    }

}
