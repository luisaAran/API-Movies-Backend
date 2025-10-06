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
/**
 * @Author: luisaAran
 * Clase que maneja las excepciones globalmente en la aplicación.
 * Utiliza @ControllerAdvice para interceptar las excepciones lanzadas
 */
public class GlobalExceptionHandler {
    /**
     * Manejo de la excepción ResourceNotFoundException.
     * @param exception
     * @return
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionBody> handleResourceNotFound(ResourceNotFoundException exception){
        ExceptionBody body = ExceptionBody.builder()
                .error("Resource Not Found")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Manejo de la excepción InvalidScoreException.
     * @param exception
     * @return
     */
    @ExceptionHandler(InvalidScoreException.class)
    public ResponseEntity<ExceptionBody> handleInvalidScore(InvalidScoreException exception){
        ExceptionBody body = ExceptionBody.builder()
                .error("Invalid Score")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Manejo de la excepción DuplicatedResourceException.
     * @param exception
     * @return
     */
    @ExceptionHandler(DuplicatedResourceException.class)
    public ResponseEntity<ExceptionBody> handleDuplicatedResource(DuplicatedResourceException exception){
        ExceptionBody body = ExceptionBody.builder()
                .error("Conflict")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    /**
     * Manejo de la excepción AuthException.
     * @param exception
     * @return
     */
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ExceptionBody> handleAuthException(AuthException exception){
        ExceptionBody body = ExceptionBody.builder()
                .error("Unauthorized")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    /**
     * Manejo de la excepción BadCredentialsException.
     * Esta excepcion es de Spring Security y se lanza cuando la contraseña
     * o el usuario son incorrectos
     * @param exception
     * @return
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionBody> handleBadCredentials(BadCredentialsException exception){
        ExceptionBody body = ExceptionBody.builder()
                .error("Unauthorized")
                .message("Credenciales inválidas")
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    /**
     * Manejo de la excepción MethodArgumentNotValidException.
     * Esta excepción se lanza cuando la validación de un DTO falla
     * @param exception
     * @return
     */
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
