package com.example.web_auth.config;

import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private Bucket loginBucket;

    @Autowired
    private Bucket forgotPasswordBucket;

    @Override
    public boolean preHandle(@SuppressWarnings("null") HttpServletRequest request,
            @SuppressWarnings("null") HttpServletResponse response, @SuppressWarnings("null") Object handler)
            throws Exception {
        String path = request.getRequestURI();

        if (path.equals("/api/auth/login")) {
            if (!loginBucket.tryConsume(1)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("Too many login attempts. Please try again later.");
                return false;
            }
        } else if (path.equals("/api/auth/forgot-password")) {
            if (!forgotPasswordBucket.tryConsume(1)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("Too many password reset attempts. Please try again later.");
                return false;
            }
        }

        return true;
    }
}