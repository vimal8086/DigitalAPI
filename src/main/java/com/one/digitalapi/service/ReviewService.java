package com.one.digitalapi.service;

import com.one.digitalapi.entity.Review;

import java.util.List;

public interface ReviewService {

    Review addReview(Review review);

    List<Review> getReviewsByBusId(Long busId);

    Double getAverageRatingForBus(Long busId);
}

