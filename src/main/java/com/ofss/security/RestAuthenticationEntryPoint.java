package com.ofss.security;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class RestAuthenticationEntryPoint
        implements AuthenticationEntryPoint {

    private static final ZoneId IST = ZoneId.of("Asia/Kolkata");

    private final ObjectMapper objectMapper;

    public RestAuthenticationEntryPoint(
            ObjectMapper objectMapper
    ) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> errorBody = new LinkedHashMap<>();

        errorBody.put("status", "401 UNAUTHORIZED");
        errorBody.put("message", "Authentication token is required");
        errorBody.put("timeStamp", OffsetDateTime.now(IST));
        errorBody.put("errors", null);

        objectMapper.writeValue(
                response.getOutputStream(),
                errorBody
        );
    }
}