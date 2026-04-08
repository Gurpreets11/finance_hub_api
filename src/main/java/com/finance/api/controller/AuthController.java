package com.finance.api.controller;

import com.finance.api.dto.request.AuthRequest;
import com.finance.api.dto.response.ApiResponse;
import com.finance.api.dto.response.AuthResponse;
import com.finance.api.service.impl.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/v1/auth/signup
     * Register a new user. Returns JWT access + refresh tokens.
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthResponse.TokenPair>> signup(
            @Valid @RequestBody AuthRequest.SignUp request) {

        AuthResponse.TokenPair tokens = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Account created successfully", tokens));
    }

    /**
     * POST /api/v1/auth/login
     * Authenticate user. Returns JWT access + refresh tokens.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse.TokenPair>> login(
            @Valid @RequestBody AuthRequest.Login request) {

        AuthResponse.TokenPair tokens = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", tokens));
    }

    /**
     * POST /api/v1/auth/refresh
     * Exchange a refresh token for a new access + refresh token pair.
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse.TokenPair>> refresh(
            @Valid @RequestBody AuthRequest.RefreshToken request) {

        AuthResponse.TokenPair tokens = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success("Token refreshed", tokens));
    }
}
