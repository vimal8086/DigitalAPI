package com.one.digitalapi.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.one.digitalapi.entity.DiscountType;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
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


    @ExceptionHandler(DiscountException.class)
    public ResponseEntity<Map<String, Object>> handleDiscountException(DiscountException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Discount not found");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Invalid input");

        return new ResponseEntity<>(Map.of("error", errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        Map<String, String> response = new HashMap<>();

        if (cause instanceof InvalidFormatException invalidFormatException) {
            String fieldName = "unknown field";
            if (!invalidFormatException.getPath().isEmpty()) {
                fieldName = invalidFormatException.getPath().get(0).getFieldName();
            }

            if (invalidFormatException.getTargetType() != null && (invalidFormatException.getTargetType().equals(Date.class) || invalidFormatException.getTargetType().equals(java.time.LocalDateTime.class) || invalidFormatException.getTargetType().equals(java.time.LocalDate.class))) {response.put("error", "Invalid date format for field '" + fieldName + "'. Please use the format 'yyyy-MM-dd'T'HH:mm:ss'.");
            }
            else if (invalidFormatException.getTargetType() != null &&
                    invalidFormatException.getTargetType().equals(DiscountType.class)) {
                response.put("error", "Invalid value for field '" + fieldName + "'. Accepted values are: PERCENTAGE, FLAT.");
            }
            else {
                response.put("error", "Invalid value for field '" + fieldName + "'. Please provide a value of the correct type.");
            }

        } else if (cause instanceof JsonParseException) {
            response.put("error", "Malformed JSON request. Please check the request syntax.");
        } else if (cause instanceof JsonMappingException) {
            response.put("error", "JSON mapping error: " + cause.getMessage());
        } else {
            response.put("error", "Invalid request body");
        }

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    // Handle other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "An unexpected error occurred");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage()); // Return the message in JSON format
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    public static ResponseEntity<Map<String, Object>> getMapResponseEntity(String message, Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();

        errorResponse.put("status", HttpStatus.NOT_FOUND.value());
        errorResponse.put("error", message);
        errorResponse.put("timestamp", System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}