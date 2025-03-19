package com.one.digitalapi.exception;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Hidden
public class GlobalExceptionHandler {

    @ExceptionHandler(BusException.class)
    public ResponseEntity<String> handleBusException(BusException ex, WebRequest request) {
        return new ResponseEntity<>("Bus Exception: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RouteException.class)
    public ResponseEntity<String> handleRouteException(RouteException ex, WebRequest request) {
        return new ResponseEntity<>("Route Exception: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // ✅ Handle validation exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Extract field errors and messages
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
