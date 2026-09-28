package com.ofss.service;

import java.util.List;

import com.ofss.dto.AdminCreateUserRequest;
import com.ofss.dto.AuthResponse;
import com.ofss.dto.LoginRequest;
import com.ofss.dto.RefreshTokenRequest;
import com.ofss.dto.RegisterRequest;
import com.ofss.dto.UserResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    UserResponse createUserByAdmin(AdminCreateUserRequest request);

    List<UserResponse> getAllUsers();
    
    AuthResponse refreshAccessToken(RefreshTokenRequest request);
    
    UserResponse getUserById(Long userId);
}