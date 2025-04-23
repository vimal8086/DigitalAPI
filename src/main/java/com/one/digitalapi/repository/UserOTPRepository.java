package com.one.digitalapi.repository;

import com.one.digitalapi.entity.UserOTP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserOTPRepository extends JpaRepository<UserOTP, Long> {
    Optional<UserOTP> findTopByEmailOrderByExpiryDesc(String email);

    Optional<UserOTP> findByEmail(String email);
}