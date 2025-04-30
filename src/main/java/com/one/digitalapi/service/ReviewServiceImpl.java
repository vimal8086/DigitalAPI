package com.one.digitalapi.service;

import com.one.digitalapi.entity.Review;
import com.one.digitalapi.logger.DefaultLogger;
import com.one.digitalapi.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private static final String CLASSNAME = "ReviewServiceImpl";

    private static final DefaultLogger LOGGER = new DefaultLogger(ReviewServiceImpl.class);

    @Autowired
    private ReviewRepository reviewRepo;

    @Override
    public Review addReview(Review review) {
        String methodName = "addReview";
        LOGGER.infoLog(CLASSNAME, methodName, "Saving review: " + review);

        review.setCreatedAt(LocalDateTime.now());
        Review savedReview = reviewRepo.save(review);

        LOGGER.infoLog(CLASSNAME, methodName, "Review saved: " + savedReview);
        return savedReview;
    }

    @Override
    public List<Review> getReviewsByBusId(Long busId) {
        String methodName = "getReviewsByBusId";
        LOGGER.infoLog(CLASSNAME, methodName, "Fetching reviews for bus ID: " + busId);

        List<Review> reviews = reviewRepo.findByBusId(busId);
        if (reviews.isEmpty()) {
            LOGGER.infoLog(CLASSNAME, methodName, "No reviews found for bus ID: " + busId);
        } else {
            LOGGER.infoLog(CLASSNAME, methodName, "Found " + reviews.size() + " reviews for bus ID: " + busId);
        }

        return reviews.isEmpty() ? Collections.emptyList() : reviews;
    }

    @Override
    public Double getAverageRatingForBus(Long busId) {
        String methodName = "getAverageRatingForBus";
        LOGGER.infoLog(CLASSNAME, methodName, "Calculating average rating for bus ID: " + busId);

        Double avg = reviewRepo.findAverageRatingByBusId(busId);
        double finalAvg = avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;

        LOGGER.infoLog(CLASSNAME, methodName, "Average rating for bus ID " + busId + " is: " + finalAvg);
        return finalAvg;
    }
}