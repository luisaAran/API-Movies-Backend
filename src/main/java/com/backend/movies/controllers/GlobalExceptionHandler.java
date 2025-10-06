package com.backend.movies.controllers;

import com.backend.movies.exceptions.AuthException;
import com.backend.movies.exceptions.DuplicatedResourceException;
import com.backend.movies.exceptions.InvalidScoreException;
import com.backend.movies.exceptions.ResourceNotFoundException;
import com.backend.movies.utils.ExceptionBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionBody> handleResourceNotFound(ResourceNotFoundException exception){
        ExceptionBody body = ExceptionBody.builder()
                .error("Resource Not Found")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
    @ExceptionHandler(InvalidScoreException.class)
    public ResponseEntity<ExceptionBody> handleInvalidScore(InvalidScoreException exception){
        ExceptionBody body = ExceptionBody.builder()
                .error("Invalid Score")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
    @ExceptionHandler(DuplicatedResourceException.class)
    public ResponseEntity<ExceptionBody> handleDuplicatedResource(DuplicatedResourceException exception){
        ExceptionBody body = ExceptionBody.builder()
                .error("Conflict")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ExceptionBody> handleAuthException(AuthException exception){
        ExceptionBody body = ExceptionBody.builder()
                .error("Unauthorized")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionBody> handleBadCredentials(BadCredentialsException exception){
        ExceptionBody body = ExceptionBody.builder()
                .error("Unauthorized")
                .message("Credenciales inválidas")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionBody> handleValidationException(MethodArgumentNotValidException exception){
        ExceptionBody body = ExceptionBody.builder()
                .error("Bad Request")
                .message("Invalid input data")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
