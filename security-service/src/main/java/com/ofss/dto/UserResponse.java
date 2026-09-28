package com.ofss.dto;

import com.ofss.enums.Role;

public record UserResponse(

        Long userId,

        String username,

        String email,

        Role role,

        boolean enabled
) {
}
