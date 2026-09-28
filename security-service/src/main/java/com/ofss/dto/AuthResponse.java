package com.ofss.dto;

import com.ofss.enums.Role;

public record AuthResponse(

        Long userId,

        String username,

        Role role,

        String token,
        
        String refreshToken
) {
}
