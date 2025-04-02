package com.one.digitalapi.config;

import com.one.digitalapi.entity.BookingDetails;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public com.github.benmanes.caffeine.cache.Cache<String, BookingDetails> bookingCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES) // Auto-remove after 15 min
                .maximumSize(1000) // Limit cache size
                .build();
    }
}

