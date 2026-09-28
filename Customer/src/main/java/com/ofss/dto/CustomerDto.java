package com.ofss.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ofss.validation.Patch;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;

public record CustomerDto(

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long customerId,
    
    
    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be greater than zero")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Long userId,
    
    
    @NotBlank(
        message = "Customer name is required",
        groups = Default.class
    )
    @Size(
        max = 100,
        message = "Customer name cannot exceed 100 characters",
        groups = {Default.class, Patch.class}
    )
    @Pattern(
        regexp = "(?s).*\\S.*",
        message = "Customer name cannot be blank",
        groups = {Default.class, Patch.class}
    )
    String customerName,

    @NotBlank(
        message = "Email is required",
        groups = Default.class
    )
    @Email(
        message = "Enter a valid email address",
        groups = {Default.class, Patch.class}
    )
    @Size(
        min = 1,
        max = 150,
        message = "Email must contain between 1 and 150 characters",
        groups = {Default.class, Patch.class}
    )
    String email,

    @NotBlank(
        message = "Mobile number is required",
        groups = Default.class
    )
    @Pattern(
        regexp = "[0-9]{10}",
        message = "Mobile number must contain exactly 10 digits",
        groups = {Default.class, Patch.class}
    )
    String mobileNumber,

    @NotBlank(
        message = "PAN number is required",
        groups = Default.class
    )
    @Pattern(
        regexp = "[A-Z]{5}[0-9]{4}[A-Z]",
        message = "PAN must follow the format ABCDE1234F",
        groups = {Default.class, Patch.class}
    )
    String panNumber

) {
}
