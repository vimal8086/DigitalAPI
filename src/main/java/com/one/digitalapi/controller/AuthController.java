package com.one.digitalapi.controller;

import com.one.digitalapi.dto.LoginRequest;
import com.one.digitalapi.dto.RefreshTokenRequest;
import com.one.digitalapi.entity.User;
import com.one.digitalapi.logger.DefaultLogger;
import com.one.digitalapi.repository.UserRepository;
import com.one.digitalapi.service.UserService;
import com.one.digitalapi.utils.JwtUtil;
import com.one.digitalapi.utils.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Management (Generate Token)", description = "APIs for generating a token using user ID and password, and verifying if the user exists")
public class AuthController {


    private static final String CLASSNAME="AuthController";

    private static final DefaultLogger LOGGER = new DefaultLogger(AuthController.class);

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

        String strMethodName="checkUserUnique";

        LOGGER.infoLog(CLASSNAME, strMethodName, "Received request to check user existence: {} "+ userId);

        Map<String, String> response = new HashMap<>();
        if (userService.isUserIdUnique(userId)) {
            response.put("message", "User ID is available");
            LOGGER.debugLog(CLASSNAME, strMethodName, "User ID '{}' is available" + userId);
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "User ID already exists");
            LOGGER.warnLog(CLASSNAME, strMethodName, "User ID '{}' already exists" + userId);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Generate a token using the user ID", description = "You can generate a token using userID and Password")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {

        String strMethodName = "login";
        LOGGER.infoLog(CLASSNAME, strMethodName, "Received login request for user: {} " + request.getUserId());

        Optional<User> userOptional = userRepository.findByUserId(request.getUserId());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Check if user is ACTIVE
            if (user.getStatus() != UserStatus.ACTIVE) {
                LOGGER.warnLog(CLASSNAME, strMethodName, "Login denied - User status is not ACTIVE: {} " + user.getStatus());
                return ResponseEntity.status(403).body(Map.of("error", "Account is " + user.getStatus() + ". Please contact support."));
            }

            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                String token = jwtUtil.generateToken(user.getUserId());
                Date expirationDate = jwtUtil.extractExpiration(token);
                long expiresAtMillis = expirationDate.getTime(); // Convert to milliseconds

                // Refresh Token Code
                String refreshToken = jwtUtil.generateRefreshToken(user.getUserId());


                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("expires_at", expirationDate.toString());
                response.put("time", expiresAtMillis);
                response.put("userId", user.getUserId());
                response.put("email", user.getEmail());
                response.put("contactNumber", user.getContactNumber());
                response.put("id", user.getId());
                response.put("admin", user.isAdmin());

                // Added Refresh Token Fields
                response.put("refreshToken", refreshToken);

                LOGGER.infoLog(CLASSNAME, strMethodName, "Login successful for user: {} " + request.getUserId());

                return ResponseEntity.ok(response);
            } else {
                LOGGER.warnLog(CLASSNAME, strMethodName, "Invalid password attempt for user: {} " + request.getUserId());
            }
        } else {
            LOGGER.warnLog(CLASSNAME, strMethodName, "Login failed - User not found: {} " + request.getUserId());
        }

        return ResponseEntity.status(401).body(Map.of("error", "Invalid Credentials"));
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Generate new access token using a valid refresh token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        try {
            // Extract username and check if refresh token is valid
            String username = jwtUtil.extractUsername(refreshToken);

            if (!jwtUtil.isTokenValid(refreshToken, username)) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired refresh token"));
            }

            // Generate new access token and new refresh token
            String newAccessToken = jwtUtil.generateToken(username);
            String newRefreshToken = jwtUtil.generateRefreshToken(username);

            Map<String, Object> response = new HashMap<>();
            response.put("access_token", newAccessToken);
            response.put("refresh_token", newRefreshToken);
            response.put("expires_at", jwtUtil.extractExpiration(newAccessToken).getTime());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Could not refresh token"));
        }
    }

}