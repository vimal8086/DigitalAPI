package com.one.digitalapi.repository;

import com.one.digitalapi.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByBusId(Long busId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.busId = :busId")
    Double findAverageRatingByBusId(@Param("busId") Long busId);

    Optional<Review> findByOrderId(String orderId);
}

