package com.example.motogp_b.service;

public interface EmailService {
    void sendOtpEmail(String toEmail, String subject, String otp);
}
