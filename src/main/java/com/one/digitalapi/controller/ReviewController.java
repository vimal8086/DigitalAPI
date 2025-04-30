package com.one.digitalapi.controller;

import com.one.digitalapi.entity.Review;
import com.one.digitalapi.logger.DefaultLogger;
import com.one.digitalapi.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
@Tag(name = "Review-Rating Management", description = "APIs for managing review-rating")
public class ReviewController {

    private static final String CLASSNAME = "ReviewController";

    private static final DefaultLogger LOGGER = new DefaultLogger(ReviewController.class);

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Add a new review-rating", description = "Creates a new rating")
    public ResponseEntity<Map<String, Object>> addReview(@Valid @RequestBody Review review) {
        String methodName = "addReview";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to add review: " + review);

        Review savedReview = reviewService.addReview(review);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Review submitted successfully.");
        response.put("rating", savedReview);

        LOGGER.infoLog(CLASSNAME, methodName, "Review saved successfully: " + savedReview);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/bus/{busId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get All rating for bus", description = "Retrieve All Rating For Bus")
    public ResponseEntity<Map<String, Object>> getReviewsForBus(@PathVariable Long busId) {
        String methodName = "getReviewsForBus";
        LOGGER.infoLog(CLASSNAME, methodName, "Fetching reviews for bus ID: " + busId);

        List<Review> reviews = reviewService.getReviewsByBusId(busId);
        Map<String, Object> response = new HashMap<>();

        if (reviews.isEmpty()) {
            response.put("message", "No reviews found for this bus.");
            response.put("rating", Collections.emptyList());
            LOGGER.infoLog(CLASSNAME, methodName, "No reviews found for bus ID: " + busId);
            return ResponseEntity.ok(response);
        }

        response.put("message", "Reviews fetched successfully.");
        response.put("rating", reviews);
        response.put("totalRating", reviews.size());
        LOGGER.infoLog(CLASSNAME, methodName, "Fetched " + reviews.size() + " reviews for bus ID: " + busId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/bus/average-rating/{busId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get All Average Rating For Bus", description = "Retrieve All Average Rating For Bus")
    public ResponseEntity<Map<String, Object>> getAverageRating(@PathVariable Long busId) {
        String methodName = "getAverageRating";
        LOGGER.infoLog(CLASSNAME, methodName, "Fetching average rating for bus ID: " + busId);

        Double avgRating = reviewService.getAverageRatingForBus(busId);
        Map<String, Object> response = new HashMap<>();

        if (avgRating == 0.0) {
            response.put("message", "No ratings available for this bus.");
            response.put("averageRating", avgRating);
            LOGGER.infoLog(CLASSNAME, methodName, "No ratings available for bus ID: " + busId);
            return ResponseEntity.ok(response);
        }

        response.put("message", "Average rating fetched successfully.");
        response.put("averageRating", avgRating);
        LOGGER.infoLog(CLASSNAME, methodName, "Average rating for bus ID " + busId + " is: " + avgRating);
        return ResponseEntity.ok(response);
    }
}