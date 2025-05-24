package com.example.motogp_b.service.impl;

import com.example.motogp_b.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResendEmailService implements EmailService {

    @Value("${resend.api.key}")
    private String apiKey;

    @Value("${resend.from.email}")
    private String fromEmail;

    @Value("${resend.from.name}")
    private String fromName;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.resend.com")
            .build();

    @Override
    public void sendOtpEmail(String toEmail, String subject, String otp) {
        try {
            System.out.println("Attempting to send email:");
            System.out.println("API Key: " + (apiKey != null ? apiKey.substring(0, 10) + "..." : "null"));
            System.out.println("From: " + fromName + " <" + fromEmail + ">");
            System.out.println("To: " + toEmail);
            String htmlContent = buildOtpEmailTemplate(otp);

            Map<String, Object> emailPayload = Map.of(
                    "from", fromName + " <" + fromEmail + ">",
                    "to", new String[]{toEmail},
                    "subject", subject,
                    "html", htmlContent
            );

            String response = webClient.post()
                    .uri("/emails")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(emailPayload)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("OTP email sent successfully to " + toEmail + ". Response: " + response);
        } catch (Exception e) {
            System.err.println("Error sending OTP email to " + toEmail + ": " + e.getMessage());
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    private String buildOtpEmailTemplate(String otp) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Password Reset OTP</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f4f4f4; }
                    .container { max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
                    .header { text-align: center; margin-bottom: 30px; }
                    .otp-code { background-color: #f8f9fa; border: 2px dashed #dee2e6; padding: 20px; text-align: center; font-size: 32px; font-weight: bold; color: #495057; letter-spacing: 5px; margin: 20px 0; }
                    .warning { background-color: #fff3cd; border: 1px solid #ffeaa7; padding: 15px; border-radius: 5px; margin: 20px 0; }
                    .footer { margin-top: 30px; padding-top: 20px; border-top: 1px solid #dee2e6; text-align: center; color: #6c757d; font-size: 14px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1 style="color: #d63384;">🏍️ MotoGP Password Reset</h1>
                        <p>We received a request to reset your password</p>
                    </div>
                    
                    <p>Hello,</p>
                    <p>Use the following OTP (One-Time Password) to reset your password:</p>
                    
                    <div class="otp-code">%s</div>
                    
                    <div class="warning">
                        <strong>⚠️ Important:</strong>
                        <ul style="margin: 10px 0; padding-left: 20px;">
                            <li>This OTP is valid for <strong>3 minutes</strong> only</li>
                            <li>Do not share this code with anyone</li>
                            <li>If you didn't request this, please ignore this email</li>
                        </ul>
                    </div>
                    
                    <p>If you have any questions, please contact our support team.</p>
                    
                    <div class="footer">
                        <p>Best regards,<br>MotoGP Team</p>
                        <p style="font-size: 12px;">This is an automated message, please do not reply to this email.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(otp);
    }
}