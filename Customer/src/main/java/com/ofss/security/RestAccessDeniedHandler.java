package com.ofss.security;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RestAccessDeniedHandler
        implements AccessDeniedHandler {

    private static final ZoneId IST = ZoneId.of("Asia/Kolkata");

    private final ObjectMapper objectMapper;

    public RestAccessDeniedHandler(
            ObjectMapper objectMapper
    ) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exception
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> errorBody = new LinkedHashMap<>();

        errorBody.put("status", "403 FORBIDDEN");
        errorBody.put(
                "message",
                "You do not have permission to perform this operation"
        );
        errorBody.put("timeStamp", OffsetDateTime.now(IST));
        errorBody.put("errors", null);

        objectMapper.writeValue(
                response.getOutputStream(),
                errorBody
        );
    }
}