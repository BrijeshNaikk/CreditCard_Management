package com.ofss.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.HttpStatus;

public record ApiError(

        HttpStatus status,

        String message,

        String timeStamp,

        List<ApiFieldError> errors
) {

    private static final ZoneId IST_ZONE =
            ZoneId.of("Asia/Kolkata");

    private static final DateTimeFormatter IST_FORMAT =
            DateTimeFormatter.ofPattern(
                    "dd-MM-yyyy hh:mm:ss a 'IST'"
            );
    
    public ApiError(
            HttpStatus status,
            String message
    ) {
        this(
                status,
                message,
                ZonedDateTime.now(IST_ZONE).format(IST_FORMAT),
                List.of()
        );
    }

    public ApiError(
            HttpStatus status,
            String message,
            List<ApiFieldError> errors
    ) {
        this(
                status,
                message,
                ZonedDateTime.now(IST_ZONE).format(IST_FORMAT),
                errors
        );
    }
}