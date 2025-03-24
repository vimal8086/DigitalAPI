package com.one.digitalapi.controller;
import com.one.digitalapi.entity.User;
import com.one.digitalapi.exception.UserException;
import com.one.digitalapi.logger.DefaultLogger;
import com.one.digitalapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

import static com.one.digitalapi.exception.GlobalExceptionHandler.getMapResponseEntity;


@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    private static final String CLASSNAME = "UserController";
    private static final DefaultLogger LOGGER = new DefaultLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user with email and password")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {

        String methodName = "registerUser";

        LOGGER.infoLog(CLASSNAME, methodName, "Received request to register a new user: " + user);

        User savedUser = userService.registerUser(user);

        LOGGER.infoLog(CLASSNAME, methodName, "User Register Successfully : " + user);

        return ResponseEntity.ok(savedUser);
    }

    // Global Exception Handling for UserException
    @ExceptionHandler(UserException.class)
    public ResponseEntity<Map<String, Object>> handleUserException(UserException ex) {
        String methodName = "handleUserException";
        LOGGER.errorLog(CLASSNAME, methodName, "UserException occurred: " + ex.getMessage());

        return getMapResponseEntity(ex.getMessage(), ex);
    }
}
