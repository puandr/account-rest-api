package com.example.accountservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration class for the application.
 *
 * This class configures Spring Security for the application, defining authentication,
 * authorization, and other security-related settings.
 *
 * Key Features:
 * - Configures a security filter chain for managing HTTP security.
 * - Provides an in-memory user details service for authentication.
 * - Defines a password encoder for encrypting user passwords.
 * - Exposes an {@link AuthenticationManager} bean for managing authentication.
 *
 * Annotations:
 * - {@link Configuration}: Marks this class as a configuration class for Spring.
 * - {@link EnableWebSecurity}: Enables Spring Security in the application.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain for the application.
     *
     * - Disables CSRF protection (not recommended for production unless used with caution).
     * - Permits unauthenticated access to specific endpoints like "/error" and "/ping".
     * - Allows public access to Swagger documentation endpoints.
     * - Requires authentication for all other requests using HTTP Basic Auth.
     * - Configures headers to allow H2 console to be displayed in a frame (useful for local development).
     *
     * @param http HttpSecurity object to configure the security filter chain
     * @return Configured SecurityFilterChain
     * @throws Exception If an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Disables CSRF protection
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error", "/ping").permitAll() // Public endpoints
                        .requestMatchers(
                                "/api/swagger-ui.html",
                                "/api/v3/api-docs/**",
                                "/api/swagger-ui/**"
                        ).permitAll() // Public Swagger documentation endpoints
                        .anyRequest().authenticated() // All other endpoints require authentication
                )
                .httpBasic(Customizer.withDefaults()) // Enables Basic Authentication
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())) // Allows H2 console in frames
                .build();
    }

    /**
     * Defines a custom in-memory user details service for authentication.
     *
     * - Creates a user with username "johndoe", password "password1" (BCrypt-encoded), and role "USER".
     * - Use only for development or testing purposes; replace with persistent user storage for production.
     *
     * @param passwordEncoder Password encoder for encoding user passwords
     * @return UserDetailsService implementation with predefined users
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user1 = User.builder()
                .username("johndoe")
                .password(passwordEncoder.encode("password1")) // Encrypt password
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user1); // In-memory user store
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
