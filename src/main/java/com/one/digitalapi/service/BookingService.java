package com.one.digitalapi.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.one.digitalapi.entity.BookingDetails;
import com.one.digitalapi.entity.Passenger;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

import com.github.benmanes.caffeine.cache.Caffeine;
import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Service
public class BookingService {

    private Cache<String, BookingDetails> bookingCache;

    @PostConstruct
    public void initCache() {
        this.bookingCache = Caffeine.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES)  // Expire after 15 minutes
                .maximumSize(1000)  // Prevent memory overflow
                .build();
    }

    public void cacheBooking(BookingDetails bookingDetails) {
        String key = bookingDetails.getBusId() + "-" + bookingDetails.getDate();
        bookingCache.put(key, bookingDetails);
    }

    public BookingDetails getCachedBooking(String busId, String date) {
        String key = busId + "-" + date;
        return bookingCache.getIfPresent(key);
    }

    public List<String> getCachedAvailableSeats(Integer busId) {
        return bookingCache.asMap().entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(busId + "-"))  // Get seats for this bus
                .flatMap(entry -> entry.getValue().getPassengerList().stream().map(Passenger::getSeatName))
                .collect(Collectors.toList());
    }
}