package com.example.motogp_b;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class MotogpBApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(MotogpBApplication.class, args);
	}

}
