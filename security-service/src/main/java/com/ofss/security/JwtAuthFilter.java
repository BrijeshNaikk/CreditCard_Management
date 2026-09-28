package com.ofss.security;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final ZoneId IST = ZoneId.of("Asia/Kolkata");

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    public JwtAuthFilter(
            JwtService jwtService,
            ObjectMapper objectMapper
    ) {
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
    }
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getServletPath();

        return path.equals("/api/auth/login")
                || path.equals("/api/auth/register") || path.equals("/api/auth/refresh");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwtToken = authorizationHeader.substring(7);

            JwtUserPrincipal user = jwtService.verifyJwtToken(jwtToken);

            if (SecurityContextHolder.getContext()
                    .getAuthentication() == null) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                user.authorities()
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

        } catch (JwtException | IllegalArgumentException exception) {

            SecurityContextHolder.clearContext();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            Map<String, Object> errorBody = new LinkedHashMap<>();
            errorBody.put("status", "401 UNAUTHORIZED");
            errorBody.put("message", "JWT token is invalid or expired");
            errorBody.put("timeStamp", OffsetDateTime.now(IST));
            errorBody.put("errors", null);

            objectMapper.writeValue(
                    response.getOutputStream(),
                    errorBody
            );
        }
    }
}