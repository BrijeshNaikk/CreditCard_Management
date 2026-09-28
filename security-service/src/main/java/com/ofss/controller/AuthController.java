package com.ofss.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ofss.dto.AdminCreateUserRequest;
import com.ofss.dto.AuthResponse;
import com.ofss.dto.LoginRequest;
import com.ofss.dto.RefreshTokenRequest;
import com.ofss.dto.RegisterRequest;
import com.ofss.dto.UserResponse;
import com.ofss.service.AuthService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        AuthResponse response = authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createUserByAdmin(
            @Valid @RequestBody AdminCreateUserRequest request
    ) {

        UserResponse response =
                authService.createUserByAdmin(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        return ResponseEntity.ok(
                authService.getAllUsers()
        );
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshAccessToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) {

        AuthResponse response =
                authService.refreshAccessToken(request);

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                authService.getUserById(userId)
        );
    }
}
