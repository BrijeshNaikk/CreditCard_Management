package com.ofss.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record AccountDto(

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long accountId,

        @NotBlank(message = "Account number is required")
        @Size(max = 20, message = "Account number cannot exceed 20 characters")
        String accountNumber,

        @NotBlank(message = "Account type is required")
        @Size(max = 20, message = "Account type cannot exceed 20 characters")
        String accountType,

        @NotNull(message = "Balance is required")
        @DecimalMin(value = "0.00", message = "Balance cannot be negative")
        @Digits(
                integer = 13,
                fraction = 2,
                message = "Balance allows up to 13 integer digits and 2 decimal places"
        )
        BigDecimal balance,

        @NotNull(message = "Customer ID is required")
        @Positive(message = "Customer ID must be positive")
        Long customerId

) {
}
