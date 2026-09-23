package com.ofss.exceptions;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex){
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(apiError);
	}
	
	
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ApiError> handleBadRequestException(BadRequestException ex){
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex){
		
		List<ApiFieldError> fieldErrors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(error -> new ApiFieldError(error.getField(),
								error.getDefaultMessage()))
				.toList();
		
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Input validation failed", fieldErrors);
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
	
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiError> handleInvalidRequestBody(HttpMessageNotReadableException ex){
		ApiError apiError = new ApiError(
				HttpStatus.BAD_REQUEST,
				"Request body is missing or contains invalid JSON or field values");
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ApiError> handleTypeMismatchException(MethodArgumentTypeMismatchException ex){
		
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Invalid value for parameter: " + ex.getName());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
		
		
	}
	
//	@ExceptionHandler(DataIntegrityViolationException.class)
//	public ResponseEntity<ApiError> handleDataIntegrityException(DataIntegrityViolationException ex){
//		
//		ApiError apiError = new ApiError(
//				HttpStatus.CONFLICT,
//				"Operation violates a database constraint. "
//				+ "Check for duplicate email/PAN, missing required values "
//						+ "or linked records that prevent deletion.");
//		
//		return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
//		
//	}
}
