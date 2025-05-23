package com.one.digitalapi.entity;

import com.one.digitalapi.utils.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "User ID is required")
    @Column(unique = true, nullable = false)
    private String userId;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "code is required")
    private String code;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    private LocalDateTime lastLogin;

    @CreationTimestamp  // Automatically sets the creation timestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp  // Automatically updates when the record is modified
    private LocalDateTime updatedAt;

    @Pattern(regexp = "\\d{10}", message = "Contact Number must be 10 digits")
    @NotBlank(message = "Contact Number is required")
    private String contactNumber;

    private String gender;

    private String name;

    @Column(name = "ADMIN", columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean admin = false; // default to false

}