package com.ofss.service;


import java.math.BigDecimal;
import java.util.List;

import com.ofss.dto.CardBalanceResponse;
import com.ofss.dto.CreditCardDto;

public interface CreditCardService {

	CreditCardDto createCreditCard(
	        CreditCardDto request,
	        String authorizationHeader
	);

    CreditCardDto getCreditCardByNumber(String cardNumber);

    List<CreditCardDto> getAllCreditCards();

    CreditCardDto updateCreditCard(
            CreditCardDto request,
            String cardNumber
    );

    CreditCardDto patchCreditCard(
            CreditCardDto request,
            String cardNumber
    );

    CreditCardDto blockCreditCard(String cardNumber);

    CreditCardDto unblockCreditCard(String cardNumber);
    
    List<CreditCardDto> getCardsForLoggedInUser(Long userId);
    
    CardBalanceResponse debitCard(
            String cardNumber,
            BigDecimal amount
    );

    CardBalanceResponse applyPayment(
            String cardNumber,
            BigDecimal amount
    );
}
