package com.example.accountservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for checking the availability of the Account Service.
 *
 * This controller provides a simple health check endpoint to ensure that the service is running
 * and accessible. It is intended for quick status checks and monitoring purposes.
 *
 * Annotations:
 * - {@link RestController}: Marks this class as a REST controller for Spring MVC.
 * - {@link RequestMapping}: Defines the base path `/ping` for the endpoint in this controller.
 * - {@link Tag}: Provides metadata for Swagger/OpenAPI documentation.
 */
@RestController
@RequestMapping("/ping")
@Tag(name = "Ping", description = "API for checking Account Service availability")
public class PingController {
    private static final Logger logger = LoggerFactory.getLogger(PingController.class);

    /**
     * Returns a simple "pong" response to indicate that the server is running.
     *
     * This endpoint serves as a basic health check and is typically used by monitoring systems
     * or to verify the service's availability.
     *
     * Logs each request for auditing purposes.
     *
     * @return a {@link ResponseEntity} with the string "pong" and an HTTP status of 200 (OK).
     */
    @Operation(summary = "Ping the server", description = "Returns a simple 'pong' response to indicate the server is running.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Server is running"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error")
    })
    @GetMapping
    public ResponseEntity<String> ping() {
        logger.info("PingController - Ping request received");
        return ResponseEntity.ok("pong");
    }
}
