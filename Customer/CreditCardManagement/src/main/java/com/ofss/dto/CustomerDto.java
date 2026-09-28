package com.ofss.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerDto (
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	Long customerId,
	
	@NotBlank(message = "Customer name is requried.")
	@Size(max = 100, message = "Customer name cannot exceed 100 characters.")
	String customerName,
	
	@NotBlank(message = "Mobile number is required.")
	@Pattern(regexp = "[0-9]{10}", message = "Mobile number must contain exactly 10 digits.")
	String mobileNumber,
	
	@NotBlank(message = "Email address is required.")
	@Email(message = "Enter a valid email address")
	@Size(max = 150, message = "Email cannot exceed 150 characters")
	String email,
	
	@NotBlank(message = "PAN number is required")
	@Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]", message = "PAN must follow the format ABCDE1234F")
	String panNumber
) {
	
}
