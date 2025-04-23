package com.one.digitalapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/protected")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Protected", description = "APIs for protected")
public class ProtectedController {

    @Operation(summary = "Protected API (Requires JWT)")
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String secureEndpoint() {
        return "This is a protected API, requires JWT token";
    }
}