package com.example.motogp_b.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt; // Hoặc import lớp UserDetails của bạn nếu không dùng JWT trực tiếp làm Principal

import java.util.Optional;

@Configuration
public class appCofig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
                return Optional.of("SYSTEM");
            }
            Object principal = authentication.getPrincipal();

            if (principal instanceof Jwt) {
                return Optional.ofNullable(((Jwt) principal).getClaimAsString("sub"));
            } else if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
                return Optional.of(((org.springframework.security.core.userdetails.UserDetails) principal).getUsername());
            } else if (principal instanceof String) {
                return Optional.of((String) principal);
            }
            return Optional.of("UNKNOWN_USER");
        };
    }
}