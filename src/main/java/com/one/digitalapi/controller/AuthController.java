package com.one.digitalapi.controller;

import com.one.digitalapi.dto.LoginRequest;
import com.one.digitalapi.entity.User;
import com.one.digitalapi.repository.UserRepository;
import com.one.digitalapi.service.UserService;
import com.one.digitalapi.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Management (Generate Token)", description = "APIs for generating a token using user ID and password, and verifying if the user exists")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/check-user/{userId}")
    @Operation(summary = "Check if the user exists using user ID", description = "Check if the user exists using user ID")
    public ResponseEntity<Map<String, String>> checkUserUnique(@PathVariable String userId) {
        Map<String, String> response = new HashMap<>();
        if (userService.isUserIdUnique(userId)) {
            response.put("message", "User ID is available");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "User ID already exists");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Generate a token using the user ID", description = "You can generate token using userID and Password")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        Optional<User> userOptional = userRepository.findByUserId(request.getUserId());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                String token = jwtUtil.generateToken(user.getUserId());

                // Return a JSON response instead of plain token
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(401).body(Map.of("error", "Invalid Credentials"));
    }
}