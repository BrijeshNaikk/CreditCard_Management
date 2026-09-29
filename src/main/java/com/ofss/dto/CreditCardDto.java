package com.ofss.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ofss.enums.CardStatus;
import com.ofss.enums.CardType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreditCardDto(

        @NotBlank(message = "Card number is required")
        @Size(
                max = 19,
                message = "Card number cannot exceed 19 characters"
        )
        String cardNumber,

        @NotNull(message = "Customer ID is required")
        @Positive(message = "Customer ID must be positive")
        Long customerId,

        @NotNull(message = "Card type is required")
        CardType cardType,

        @NotNull(message = "Credit limit is required")
        @DecimalMin(
                value = "0.01",
                message = "Credit limit must be greater than zero"
        )
        @Digits(
                integer = 13,
                fraction = 2,
                message = "Credit limit allows up to 13 integer digits and 2 decimal places"
        )
        BigDecimal creditLimit,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        BigDecimal availableCredit,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        BigDecimal outstandingAmount,

        @NotNull(message = "Expiry date is required")
        LocalDate expiryDate,

        @NotNull(message = "Card status is required")
        CardStatus cardStatus

) {
}