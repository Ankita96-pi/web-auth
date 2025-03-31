package com.example.web_auth.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimitConfig {

    @Bean
    public Bucket loginBucket() {
        // 5 requests per minute for login attempts
        Bandwidth limit = Bandwidth.classic(100, Refill.intervally(5, java.time.Duration.ofMinutes(1)));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @Bean
    public Bucket forgotPasswordBucket() {
        // 3 requests per hour for forgot password
        Bandwidth limit = Bandwidth.classic(100, Refill.intervally(3, java.time.Duration.ofHours(1)));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}