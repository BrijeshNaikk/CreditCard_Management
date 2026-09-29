package com.ofss.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

public record CardAmountRequest(

        @NotNull(message = "Amount is required")
        @DecimalMin(
                value = "0.01",
                message = "Amount must be greater than zero"
        )
        @Digits(
                integer = 13,
                fraction = 2,
                message = "Amount can contain up to 13 digits and 2 decimal places"
        )
        BigDecimal amount
) {
}