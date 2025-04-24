package com.example.motogp_b.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
public class appCofig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of("TVK");
    }
}
