package com.ofss.client;

public record SecurityUserResponse(

        Long userId,

        String username,

        String email,

        String role,

        boolean enabled
) {
}
