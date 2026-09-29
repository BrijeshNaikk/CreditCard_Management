package com.ofss.dto;

import java.math.BigDecimal;

public record CardBalanceResponse(

        String cardNumber,

        Long customerId,

        BigDecimal availableCredit,

        BigDecimal outstandingAmount
) {
}