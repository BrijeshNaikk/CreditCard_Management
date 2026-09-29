package com.ofss.controller;

import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ofss.dto.CardAmountRequest;
import com.ofss.dto.CardBalanceResponse;
import com.ofss.dto.CreditCardDto;
import com.ofss.security.JwtUserPrincipal;
import com.ofss.service.CreditCardService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/credit-cards")
public class CreditCardController {

    private final CreditCardService creditCardService;

    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    // POST /api/credit-cards
    @PostMapping
    public ResponseEntity<CreditCardDto> createCreditCard(
            @Valid @RequestBody CreditCardDto request,
            @RequestHeader(HttpHeaders.AUTHORIZATION)
            String authorizationHeader
    ) {
        CreditCardDto creditCard =
                creditCardService.createCreditCard(
                        request,
                        authorizationHeader
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(creditCard);
    }

    // GET /api/credit-cards/{cardNumber}
    @GetMapping("/{cardNumber}")
    public ResponseEntity<CreditCardDto> getCreditCardByNumber(
            @PathVariable("cardNumber") String cardNumber
    ) {

        CreditCardDto creditCard =
                creditCardService.getCreditCardByNumber(cardNumber);

        return ResponseEntity.ok(creditCard);
    }

    // GET /api/credit-cards
    @GetMapping
    public ResponseEntity<List<CreditCardDto>> getAllCreditCards() {

        List<CreditCardDto> creditCards =
                creditCardService.getAllCreditCards();

        return ResponseEntity.ok(creditCards);
    }

    // PUT /api/credit-cards/{cardNumber}
    @PutMapping("/{cardNumber}")
    public ResponseEntity<CreditCardDto> updateCreditCard(
            @Valid @RequestBody CreditCardDto request,
            @PathVariable("cardNumber") String cardNumber
    ) {

        CreditCardDto creditCard =
                creditCardService.updateCreditCard(
                        request,
                        cardNumber
                );

        return ResponseEntity.ok(creditCard);
    }

    // PATCH /api/credit-cards/{cardNumber}
    @PatchMapping("/{cardNumber}")
    public ResponseEntity<CreditCardDto> patchCreditCard(
            @RequestBody CreditCardDto request,
            @PathVariable("cardNumber") String cardNumber
    ) {

        CreditCardDto creditCard =
                creditCardService.patchCreditCard(
                        request,
                        cardNumber
                );

        return ResponseEntity.ok(creditCard);
    }

    // PATCH /api/credit-cards/{cardNumber}/block
    @PatchMapping("/{cardNumber}/block")
    public ResponseEntity<CreditCardDto> blockCreditCard(
            @PathVariable("cardNumber") String cardNumber
    ) {

        CreditCardDto creditCard =
                creditCardService.blockCreditCard(cardNumber);

        return ResponseEntity.ok(creditCard);
    }

    // PATCH /api/credit-cards/{cardNumber}/unblock
    @PatchMapping("/{cardNumber}/unblock")
    public ResponseEntity<CreditCardDto> unblockCreditCard(
            @PathVariable("cardNumber") String cardNumber
    ) {

        CreditCardDto creditCard =
                creditCardService.unblockCreditCard(cardNumber);

        return ResponseEntity.ok(creditCard);
    }
    
    @GetMapping("/me")
    public ResponseEntity<List<CreditCardDto>> getMyCards(
            @AuthenticationPrincipal JwtUserPrincipal loggedInUser
    ) {

        List<CreditCardDto> cards = creditCardService
                .getCardsForLoggedInUser(loggedInUser.userId());

        return ResponseEntity.ok(cards);
    }
    
    @PostMapping("/{cardNumber}/debit")
    public ResponseEntity<CardBalanceResponse> debitCard(
            @PathVariable String cardNumber,
            @Valid @RequestBody CardAmountRequest request
    ) {
        CardBalanceResponse response =
                creditCardService.debitCard(
                        cardNumber,
                        request.amount()
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{cardNumber}/payment")
    public ResponseEntity<CardBalanceResponse> applyPayment(
            @PathVariable String cardNumber,
            @Valid @RequestBody CardAmountRequest request
    ) {
        CardBalanceResponse response =
                creditCardService.applyPayment(
                        cardNumber,
                        request.amount()
                );

        return ResponseEntity.ok(response);
    }
}
