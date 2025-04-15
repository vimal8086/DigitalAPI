package com.one.digitalapi.repository;

import com.one.digitalapi.entity.PassengerRef;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PassengerRepository extends JpaRepository<PassengerRef, Integer> {
    List<PassengerRef> findByUserId(String userId);
}
