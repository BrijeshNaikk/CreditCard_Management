package com.ofss.exceptions;

public record ApiFieldError(
		String field,
		String message
		) {

}
