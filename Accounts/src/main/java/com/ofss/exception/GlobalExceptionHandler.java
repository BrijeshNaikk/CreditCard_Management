package com.ofss.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Account or customer was not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(
            ResourceNotFoundException ex
    ) {

        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(apiError);
    }

    // A service-level validation rule failed
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(
            BadRequestException ex
    ) {

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiError);
    }

    // AccountDto validation failed
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(
            MethodArgumentNotValidException ex
    ) {

        List<ApiFieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ApiFieldError(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Input validation failed",
                fieldErrors
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiError);
    }

    // Missing body, malformed JSON or incompatible field values
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleInvalidRequestBody(
            HttpMessageNotReadableException ex
    ) {

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Request body is missing or contains invalid JSON or field values"
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiError);
    }

    // Example: /api/accounts/abc when accountId must be a Long
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex
    ) {

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Invalid value for parameter: " + ex.getName()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiError);
    }

    // A database constraint prevented the account operation
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex
    ) {

        logger.error("Account database operation failed", ex);

        ApiError apiError = new ApiError(
                HttpStatus.CONFLICT,
                "Account operation violates a database constraint. "
                        + "Check for a duplicate account number, "
                        + "missing required values or linked records."
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(apiError);
    }

    // Customer lookup could not be completed
    @ExceptionHandler(CustomerServiceUnavailableException.class)
    public ResponseEntity<ApiError> handleCustomerServiceUnavailableException(
            CustomerServiceUnavailableException ex
    ) {

        logger.error("Customer service lookup failed", ex);

        ApiError apiError = new ApiError(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Customer service is currently unavailable. Please try again later."
        );

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(apiError);
    }
}