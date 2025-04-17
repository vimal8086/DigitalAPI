package com.one.digitalapi.service;
import com.one.digitalapi.entity.User;
import com.one.digitalapi.exception.UserException;
import com.one.digitalapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public boolean isUserIdUnique(String userId) {
        return userRepository.findByUserId(userId).isEmpty();
    }

    public User registerUser(User user) {
        // Check if user already exists

        Optional<User> existingUser = userRepository.findByUserId(user.getUserId());

        if (existingUser.isPresent()) {
            throw new UserException("User ID already exists!");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email is already taken");
        }

        user.setLastLogin(LocalDateTime.now());

        // 🔹 Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User updateUser(User updatedUserData) {

        User existingUser = userRepository.findById(updatedUserData.getId())
                .orElseThrow(() -> new UserException("User not found"));

        // Optional: avoid overwriting sensitive data unless explicitly allowed
        if (updatedUserData.getEmail() != null) {
            if (!existingUser.getEmail().equals(updatedUserData.getEmail())
                    && userRepository.existsByEmail(updatedUserData.getEmail())) {
                throw new UserException("Email already in use");
            }
            existingUser.setEmail(updatedUserData.getEmail());
        }

        if (updatedUserData.getPassword() != null && updatedUserData.getPassword().length() >= 8) {
            existingUser.setPassword(passwordEncoder.encode(updatedUserData.getPassword()));

            if (!existingUser.getContactNumber().equals(updatedUserData.getContactNumber())
                    && userRepository.existsByContactNumber(updatedUserData.getContactNumber())) {
                throw new UserException("Contact already in use");
            }
            existingUser.setContactNumber(updatedUserData.getContactNumber());
        }

        if (updatedUserData.getName() != null) {
            existingUser.setName(updatedUserData.getName());
        }

        if (updatedUserData.getGender() != null) {
            existingUser.setGender(updatedUserData.getGender());
        }

        return userRepository.save(existingUser);
    }
}