package com.ofss.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.ofss.entity.AppUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

    @Value("${jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    public String generateJwtToken(AppUser user) {

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId", user.getUserId())
                .claim("roles", List.of(user.getRole().name()))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSecretKey())
                .compact();
    }

    public JwtUserPrincipal verifyJwtToken(String jwtToken) {

        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();

        Long userId = claims.get("userId", Long.class);
        String username = claims.getSubject();

        List<?> roles = claims.get("roles", List.class);

        List<GrantedAuthority> authorities = roles.stream()
                .map(Object::toString)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .map(GrantedAuthority.class::cast)
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
