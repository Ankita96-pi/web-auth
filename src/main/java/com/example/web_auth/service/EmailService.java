package com.example.web_auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendVerificationEmail(String toEmail, String token) {
        try {
            logger.info("Attempting to send verification email to: {}", toEmail);
            logger.info("Using sender email: {}", fromEmail);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Verify your email address");
            message.setText("Please click the link below to verify your email address:\n\n" +
                    "http://localhost:8080/api/auth/verify-email?token=" + token);

            logger.info("Email message prepared, attempting to send...");
            mailSender.send(message);
            logger.info("Verification email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            logger.error("Failed to send verification email to: {}", toEmail, e);
            logger.error("Error details: {}", e.getMessage());
            logger.error("Stack trace: ", e);
            throw new RuntimeException("Failed to send verification email: " + e.getMessage());
        }
    }

    public void sendPasswordResetEmail(String toEmail, String token) {
        try {
            logger.info("Attempting to send password reset email to: {}", toEmail);
            logger.info("Using sender email: {}", fromEmail);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Reset your password");
            message.setText("Please click the link below to reset your password:\n\n" +
                    "http://localhost:5173/reset-password?token=" + token + "\n\n" +
                    "This link will expire in 1 hour.\n\n" +
                    "If you didn't request this password reset, please ignore this email.");

            logger.info("Email message prepared, attempting to send...");
            mailSender.send(message);
            logger.info("Password reset email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            logger.error("Failed to send password reset email to: {}", toEmail, e);
            logger.error("Error details: {}", e.getMessage());
            logger.error("Stack trace: ", e);
            throw new RuntimeException("Failed to send password reset email: " + e.getMessage());
        }
    }
}