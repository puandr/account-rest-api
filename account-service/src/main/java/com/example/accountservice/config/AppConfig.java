package com.example.accountservice.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for application-wide bean definitions.
 *
 * This class is responsible for defining and configuring Spring beans that are
 * used throughout the application.
 *
 * Responsibilities:
 * - Configures a {@link RestTemplate} bean for making HTTP requests.
 */
@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}

