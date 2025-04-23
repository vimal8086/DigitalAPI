package com.one.digitalapi.controller;
import com.one.digitalapi.entity.User;
import com.one.digitalapi.exception.UserException;
import com.one.digitalapi.logger.DefaultLogger;
import com.one.digitalapi.repository.UserRepository;
import com.one.digitalapi.service.UserService;
import com.one.digitalapi.utils.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;
import java.util.Optional;

import static com.one.digitalapi.exception.GlobalExceptionHandler.getMapResponseEntity;


@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    private static final String CLASSNAME = "UserController";
    private static final DefaultLogger LOGGER = new DefaultLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user with email and password")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {

        String methodName = "registerUser";

        LOGGER.infoLog(CLASSNAME, methodName, "Received request to register a new user: " + user);

        User savedUser = userService.registerUser(user);

        LOGGER.infoLog(CLASSNAME, methodName, "User Register Successfully : " + user);

        return ResponseEntity.ok(savedUser);
    }

    @PatchMapping("/update")
    @Operation(summary = "Update user profile", description = "Allows a user to partially update profile information")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateUserProfile(@RequestBody User updatedFields) {
        String methodName = "updateUserProfile";

        LOGGER.infoLog(CLASSNAME, methodName, "Received PATCH update for userId: " + updatedFields);

        User updatedUser = userService.updateUser(updatedFields);

        LOGGER.infoLog(CLASSNAME, methodName, "User profile patched for userId: " + updatedFields);

        return ResponseEntity.ok(updatedUser);
    }


    @PutMapping("/users/delete/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteUserAccount(@PathVariable String userId) {

        Optional<User> optionalUser = userRepository.findByUserId(userId);

        if (optionalUser.isPresent()) {

            User user = optionalUser.get();

            if (user.getStatus() == UserStatus.ACTIVE) {

                user.setStatus(UserStatus.DELETE);

                userRepository.save(user);

                return ResponseEntity.ok("User account marked as DELETED.");

            } else {
                return ResponseEntity.badRequest().body("User is not ACTIVE.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Global Exception Handling for UserException
    @ExceptionHandler(UserException.class)
    public ResponseEntity<Map<String, Object>> handleUserException(UserException ex) {
        String methodName = "handleUserException";
        LOGGER.errorLog(CLASSNAME, methodName, "UserException occurred: " + ex.getMessage());

        return getMapResponseEntity(ex.getMessage(), ex);
    }
}