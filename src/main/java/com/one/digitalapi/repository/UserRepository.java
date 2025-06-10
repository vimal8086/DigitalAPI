package com.one.digitalapi.repository;

import com.one.digitalapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserId(String userId);

    boolean existsByEmail(String email);

    boolean existsByContactNumber(String contact);

    Optional<User> findByEmail(String email);

    Optional<User> findByContactNumber(String contact);

    long countByAdmin(boolean admin);
}
