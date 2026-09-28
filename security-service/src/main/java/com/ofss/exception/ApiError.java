package com.ofss.exception;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.http.HttpStatus;

public record ApiError(

        HttpStatus status,

        String message,

        OffsetDateTime timeStamp,

        List<ApiFieldError> errors
) {

    private static final ZoneId IST = ZoneId.of("Asia/Kolkata");

    public ApiError(
            HttpStatus status,
            String message,
            List<ApiFieldError> errors
    ) {
        this(status, message, OffsetDateTime.now(IST), errors);
    }
}

