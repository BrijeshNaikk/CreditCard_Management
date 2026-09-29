package com.ofss.security;

import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenValidator {

    @Value("${jwt.secret-key}")
    private String jwtSecretKey;

    public JwtUserPrincipal validateToken(String jwtToken) {

        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();

        Long userId = claims.get("userId", Long.class);

        String username = claims.getSubject();

        List<?> roles = claims.get("roles", List.class);

        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException(
                    "JWT token does not contain any role"
            );
        }

        List<GrantedAuthority> authorities = roles.stream()
                .map(Object::toString)
                .map(role -> (GrantedAuthority)
                        new SimpleGrantedAuthority("ROLE_" + role)
                )
                .toList();

        return new JwtUserPrincipal(
                userId,
                username,
                authorities
        );
    }

    private SecretKey getSecretKey() {

        return Keys.hmacShaKeyFor(
                jwtSecretKey.getBytes(StandardCharsets.UTF_8)
        );
    }
}
