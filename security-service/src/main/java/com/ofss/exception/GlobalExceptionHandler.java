package com.ofss.exception;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(
            BadRequestException exception
    ) {

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                null
        );

        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiError> handleUnauthorizedException(
            UnauthorizedException exception
    ) {

        ApiError apiError = new ApiError(
                HttpStatus.UNAUTHORIZED,
                exception.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(
            MethodArgumentNotValidException exception
    ) {

        List<ApiFieldError> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toApiFieldError)
                .toList();

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                errors
        );

        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDatabaseException(
            DataIntegrityViolationException exception
    ) {

        ApiError apiError = new ApiError(
                HttpStatus.CONFLICT,
                "Username or email already exists",
                null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpectedException(
            Exception exception
    ) {

        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                null
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(apiError);
    }

    private ApiFieldError toApiFieldError(FieldError fieldError) {

        return new ApiFieldError(
                fieldError.getField(),
                fieldError.getDefaultMessage()
        );
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(
            ResourceNotFoundException exception
    ) {

        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                null
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(apiError);
    }
}
