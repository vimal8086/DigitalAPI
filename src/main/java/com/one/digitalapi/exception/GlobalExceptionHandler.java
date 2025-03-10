package com.one.digitalapi.exception;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

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
}
