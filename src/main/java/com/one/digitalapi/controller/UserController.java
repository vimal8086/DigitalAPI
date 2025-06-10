package com.one.digitalapi.controller;
import com.one.digitalapi.dto.ChangePasswordRequest;
import com.one.digitalapi.entity.User;
import com.one.digitalapi.exception.UserException;
import com.one.digitalapi.logger.DefaultLogger;
import com.one.digitalapi.repository.UserRepository;
import com.one.digitalapi.service.OtpService;
import com.one.digitalapi.service.UserService;
import com.one.digitalapi.utils.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
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

    @Autowired
    private OtpService otpService;

    private final PasswordEncoder passwordEncoder;

    public UserController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user with email and password")
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

    @PutMapping("/delete/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteUserAccount(@PathVariable String userId) {

        Optional<User> optionalUser = userRepository.findByUserId(userId);

        Map<String, String> response = new HashMap<>();

        if (optionalUser.isPresent()) {

            User user = optionalUser.get();

            if (user.getStatus() == UserStatus.ACTIVE) {
                user.setStatus(UserStatus.DELETE);
                userRepository.save(user);

                response.put("message", "User account marked as DELETED.");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "User is not ACTIVE.");
                return ResponseEntity.badRequest().body(response);
            }

        } else {
            response.put("error", "User not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get user details", description = "Returns user profile based on userId")
    public ResponseEntity<?> getUserById(@PathVariable String userId) {
        String methodName = "getUserById";
        LOGGER.infoLog(CLASSNAME, methodName, "Fetching user with ID: " + userId);

        Optional<User> userOpt = userRepository.findByUserId(userId);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return ResponseEntity.ok(user);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("error", "User not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/change-password/send-otp")
    public ResponseEntity<?> sendOtp(@RequestParam String email) {

        if (!userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email not registered."));
        }

        otpService.sendOtp(email);

        return ResponseEntity.ok(Map.of("message", "OTP sent to registered email"));
    }

    @PostMapping("/change-password/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {

        boolean isVerified = otpService.verifyOtp(email, otp);

        if (isVerified) {
            return ResponseEntity.ok(Map.of("message", "OTP verified successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of("error", "Invalid or expired OTP.")
            );
        }
    }

    @PutMapping("/change-password")
    @Operation(summary = "Change Password With OTP (Forgot Password)", description = "If Any User Forgot his password then use this API")
    public ResponseEntity<?> changePassword(@RequestParam String email,
                                            @RequestParam String newPassword) {
        if (!otpService.isOtpVerified(email)) {
            return ResponseEntity.badRequest().body(Map.of("error","OTP verification required."));
        }

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return ResponseEntity.notFound().build();

        User user = userOpt.get();

        String mobileNumber = user.getContactNumber();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("mobileNumber", mobileNumber,
                "message", "Password changed successfully."));
    }

    @PostMapping("/change-existing-password")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Change Existing Password using current password (Change Password)", description = "If Any User need to change his password then use this API, Only Login User Can Access this API")
    public ResponseEntity<?> changeExistingPassword(@RequestBody @Valid ChangePasswordRequest request) {
        String method = "changeExistingPassword";
        LOGGER.infoLog(CLASSNAME, method, "Password change request");

        try {
            // Get mobile number of currently logged-in user
            String contactNumber = SecurityContextHolder.getContext().getAuthentication().getName();

            // Fetch user from database
            Optional<User> optionalUser = userRepository.findByContactNumber(contactNumber);
            if (optionalUser.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "User not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }


            User currentUser = optionalUser.get();

            // Check if old password matches
            if (!passwordEncoder.matches(request.getOldPassword(), currentUser.getPassword())) {
                return ResponseEntity.badRequest().body(Map.of("error","Old password is incorrect"));
            }

            // Check if new and confirm password match
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                return ResponseEntity.badRequest().body(Map.of("error","New password and confirm password do not match"));
            }

            // Check if new password is same as old password
            if (passwordEncoder.matches(request.getNewPassword(), currentUser.getPassword())) {
                return ResponseEntity.badRequest().body(Map.of("error","New password must be different from the old password"));
            }

            // Set and encode new password
            currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(currentUser);

            return ResponseEntity.ok(Map.of("mobileNumber", contactNumber,
                    "message", "Password changed successfully"));

        } catch (Exception e) {
            LOGGER.errorLog(CLASSNAME, method, "Password change failed: " + e.getMessage());

            Map<String, Object> response = new HashMap<>();

            response.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping("/count-by-type")
    @PreAuthorize("hasRole('ADMIN')") // Optional: restrict to admins only
    @Operation(summary = "Get count of admin and regular users", description = "Returns total count of admin and application (non-admin) users")
    public ResponseEntity<Map<String, Long>> getUserCountByType() {
        long totalAdminUsers = userRepository.countByAdmin(true);
        long totalApplicationUsers = userRepository.countByAdmin(false);

        Map<String, Long> response = new HashMap<>();
        response.put("totalAdminUsers", totalAdminUsers);
        response.put("totalApplicationUsers", totalApplicationUsers);

        return ResponseEntity.ok(response);
    }

    // Global Exception Handling for UserException
    @ExceptionHandler(UserException.class)
    public ResponseEntity<Map<String, Object>> handleUserException(UserException ex) {
        String methodName = "handleUserException";
        LOGGER.errorLog(CLASSNAME, methodName, "UserException occurred: " + ex.getMessage());

        return getMapResponseEntity(ex.getMessage(), ex);
    }
}