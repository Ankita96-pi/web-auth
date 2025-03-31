package com.example.web_auth.service;

import com.example.web_auth.dto.AuthResponse;
import com.example.web_auth.dto.ForgotPasswordRequest;
import com.example.web_auth.dto.LoginRequest;
import com.example.web_auth.dto.RegisterRequest;
import com.example.web_auth.dto.ResetPasswordRequest;
import com.example.web_auth.exception.UserNotFoundException;
import com.example.web_auth.model.User;
import com.example.web_auth.repository.UserRepository;
import com.example.web_auth.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private EmailService emailService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        try {
            logger.info("Attempting to register user with email: {}", request.getEmail());

            if (userRepository.existsByEmail(request.getEmail())) {
                logger.warn("Registration failed: Email already exists - {}", request.getEmail());
                throw new RuntimeException("Email already registered");
            }

            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setPhoneNumber(request.getPhoneNumber());
            user.setVerificationToken(UUID.randomUUID().toString());
            user.setEnabled(true);

            User savedUser = userRepository.save(user);
            logger.info("User registered successfully with ID: {}", savedUser.getId());

            String emailError = null;
            try {
                emailService.sendVerificationEmail(user.getEmail(), user.getVerificationToken());
                logger.info("Verification email sent to: {}", user.getEmail());
            } catch (Exception e) {
                emailError = e.getMessage();
                logger.error("Failed to send verification email: {}", e.getMessage(), e);
                // Don't throw the exception, just log it
            }

            UserDetails userDetails = org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getPassword())
                    .authorities("ROLE_USER")
                    .build();
            String token = jwtTokenProvider.generateToken(userDetails);

            String message = "Registration successful.";
            if (emailError != null) {
                message += " However, there was an issue sending the verification email: " + emailError;
            } else {
                message += " Please check your email to verify your account.";
            }

            return new AuthResponse(token, message);
        } catch (Exception e) {
            logger.error("Registration failed: {}", e.getMessage(), e);
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    public AuthResponse login(LoginRequest request) {
        try {
            logger.info("Attempting login for user: {}", request.getEmail());

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("User not found. Please register first."));

            if (!user.isEnabled()) {
                throw new RuntimeException("Account is disabled. Please verify your email first.");
            }

            if (!user.isVerified()) {
                throw new RuntimeException("Please verify your email before logging in.");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenProvider.generateToken(userDetails);

            logger.info("Login successful for user: {}", request.getEmail());
            return new AuthResponse(token, "Login successful");
        } catch (UserNotFoundException e) {
            logger.error("Login failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Login failed: {}", e.getMessage(), e);
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }

    @Transactional
    public AuthResponse verifyEmail(String token) {
        try {
            logger.info("Attempting to verify email with token");

            User user = userRepository.findByVerificationToken(token)
                    .orElseThrow(() -> new RuntimeException("Invalid verification token"));

            user.setEnabled(true);
            user.setIsVerified(true);
            user.setVerificationToken(null);
            userRepository.save(user);

            logger.info("Email verified successfully for user: {}", user.getEmail());
            return new AuthResponse(null, "Email verified successfully");
        } catch (Exception e) {
            logger.error("Email verification failed: {}", e.getMessage(), e);
            throw new RuntimeException("Email verification failed: " + e.getMessage());
        }
    }

    @Transactional
    public AuthResponse forgotPassword(ForgotPasswordRequest request) {
        try {
            logger.info("Processing forgot password request for email: {}", request.getEmail());

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            String resetToken = UUID.randomUUID().toString();
            user.setResetToken(resetToken);
            user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
            userRepository.save(user);

            try {
                emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
                logger.info("Password reset email sent successfully to: {}", user.getEmail());
                return new AuthResponse(null, "Password reset instructions sent to your email");
            } catch (Exception e) {
                logger.error("Failed to send password reset email: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to send password reset email: " + e.getMessage());
            }
        } catch (UserNotFoundException e) {
            logger.error("Forgot password failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Forgot password failed: {}", e.getMessage(), e);
            throw new RuntimeException("Forgot password failed: " + e.getMessage());
        }
    }

    @Transactional
    public AuthResponse resetPassword(ResetPasswordRequest request) {
        try {
            logger.info("Processing password reset request");

            User user = userRepository.findByResetToken(request.getToken())
                    .orElseThrow(() -> new RuntimeException("Invalid or expired reset token"));

            if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
                logger.warn("Reset token expired for user: {}", user.getEmail());
                throw new RuntimeException("Reset token has expired");
            }

            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            user.setResetToken(null);
            user.setResetTokenExpiry(null);
            userRepository.save(user);

            logger.info("Password reset successful for user: {}", user.getEmail());
            return new AuthResponse(null, "Password reset successful");
        } catch (Exception e) {
            logger.error("Password reset failed: {}", e.getMessage(), e);
            throw new RuntimeException("Password reset failed: " + e.getMessage());
        }
    }
}