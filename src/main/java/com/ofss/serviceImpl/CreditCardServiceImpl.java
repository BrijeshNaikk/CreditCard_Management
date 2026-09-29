package com.ofss.serviceImpl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ofss.dto.CardBalanceResponse;
import com.ofss.dto.CreditCardDto;
import com.ofss.entity.CreditCard;
import com.ofss.enums.CardStatus;
import com.ofss.exception.BadRequestException;
import com.ofss.exception.DuplicateResourceException;
import com.ofss.exception.ResourceNotFoundException;
import com.ofss.repository.CreditCardRepository;
import com.ofss.service.CreditCardService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.ofss.client.CustomerServiceClient;
@Service
@Transactional
public class CreditCardServiceImpl implements CreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final Validator validator;
    private final CustomerServiceClient customerServiceClient;

    public CreditCardServiceImpl(
            CreditCardRepository creditCardRepository,
            Validator validator,
            CustomerServiceClient customerServiceClient
    ) {
        this.creditCardRepository = creditCardRepository;
        this.validator = validator;
        this.customerServiceClient = customerServiceClient;
    }

    // Issue a new credit card
    @Override
    public CreditCardDto createCreditCard(
            CreditCardDto request,
            String authorizationHeader
    ) {
        validateCardData(request);

        if (creditCardRepository.existsById(request.cardNumber())) {
            throw new DuplicateResourceException(
                    "Card number already exists"
            );
        }

        customerServiceClient.validateCustomer(
                request.customerId(),
                authorizationHeader
        );

        CreditCard creditCard = new CreditCard();

        creditCard.setCardNumber(request.cardNumber());
        creditCard.setOutstandingAmount(BigDecimal.ZERO);
        creditCard.setCustomerId(request.customerId());

        copyFields(request, creditCard);

        CreditCard savedCard =
                creditCardRepository.saveAndFlush(creditCard);

        return toDto(savedCard);
    }

    // Retrieve one credit card
    @Override
    @Transactional(readOnly = true)
    public CreditCardDto getCreditCardByNumber(String cardNumber) {

        CreditCard creditCard = findCardByNumber(cardNumber);

        return toDto(creditCard);
    }

    // Retrieve all credit cards
    @Override
    @Transactional(readOnly = true)
    public List<CreditCardDto> getAllCreditCards() {

        return creditCardRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    // PUT: Replace all writable card details
    @Override
    public CreditCardDto updateCreditCard(
            CreditCardDto request,
            String cardNumber
    ) {

        CreditCard creditCard = findCardByNumber(cardNumber);

        validateCardData(request);

        validateCardNumberUnchanged(
                request.cardNumber(),
                cardNumber
        );
        
        validateCustomerIdUnchanged(
                request.customerId(),
                creditCard.getCustomerId()
        );

        copyFields(request, creditCard);

        CreditCard updatedCard =
                creditCardRepository.saveAndFlush(creditCard);

        return toDto(updatedCard);
    }

    // PATCH: Update only supplied non-null fields
    @Override
    public CreditCardDto patchCreditCard(
            CreditCardDto request,
            String cardNumber
    ) {

        if (request == null) {
            throw new BadRequestException("Request body is required");
        }

        CreditCard creditCard = findCardByNumber(cardNumber);

        validateCardNumberUnchanged(
                request.cardNumber(),
                cardNumber
        );
        
        validateCustomerIdUnchanged(
                request.customerId(),
                creditCard.getCustomerId()
        );

        CreditCardDto mergedRequest = new CreditCardDto(
                creditCard.getCardNumber(),

                creditCard.getCustomerId(),

                request.cardType() != null
                        ? request.cardType()
                        : creditCard.getCardType(),

                request.creditLimit() != null
                        ? request.creditLimit()
                        : creditCard.getCreditLimit(),

                creditCard.getAvailableCredit(),

                creditCard.getOutstandingAmount(),

                request.expiryDate() != null
                        ? request.expiryDate()
                        : creditCard.getExpiryDate(),

                request.cardStatus() != null
                        ? request.cardStatus()
                        : creditCard.getCardStatus()
        );

        validateCardData(mergedRequest);

        copyFields(mergedRequest, creditCard);

        CreditCard updatedCard =
                creditCardRepository.saveAndFlush(creditCard);

        return toDto(updatedCard);
    }

    // Block a credit card
    @Override
    public CreditCardDto blockCreditCard(String cardNumber) {

        CreditCard creditCard = findCardByNumber(cardNumber);

        creditCard.setCardStatus(CardStatus.BLOCKED);

        CreditCard updatedCard =
                creditCardRepository.saveAndFlush(creditCard);

        return toDto(updatedCard);
    }

    // Unblock a credit card
    @Override
    public CreditCardDto unblockCreditCard(String cardNumber) {

        CreditCard creditCard = findCardByNumber(cardNumber);

        creditCard.setCardStatus(CardStatus.ACTIVE);

        CreditCard updatedCard =
                creditCardRepository.saveAndFlush(creditCard);

        return toDto(updatedCard);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CreditCardDto> getCardsForLoggedInUser(Long userId) {

        return creditCardRepository.findAllByCustomerId(userId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    // Find an existing card
    private CreditCard findCardByNumber(String cardNumber) {

        if (cardNumber == null || cardNumber.isBlank()) {
            throw new BadRequestException(
                    "Card number is required"
            );
        }

        return creditCardRepository.findById(cardNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "CreditCard",
                                cardNumber
                        )
                );
    }

    // The primary key cannot be changed through PUT or PATCH.
    private void validateCardNumberUnchanged(
            String requestedCardNumber,
            String existingCardNumber
    ) {

        if (requestedCardNumber != null
                && !requestedCardNumber.equals(existingCardNumber)) {

            throw new BadRequestException(
                    "Card number cannot be changed"
            );
        }
    }

    // Apply the validation annotations from CreditCardDto
    private void validateCardData(CreditCardDto request) {

        if (request == null) {
            throw new BadRequestException(
                    "Request body is required"
            );
        }

        Set<ConstraintViolation<CreditCardDto>> violations =
                validator.validate(request);

        if (!violations.isEmpty()) {

            String message = violations.stream()
                    .map(violation ->
                            violation.getPropertyPath()
                                    + ": "
                                    + violation.getMessage()
                    )
                    .sorted()
                    .collect(Collectors.joining("; "));

            throw new BadRequestException(message);
        }
    }

    // Copy writable fields while preserving the outstanding amount.
    private void copyFields(
            CreditCardDto request,
            CreditCard creditCard
    ) {

        BigDecimal outstandingAmount =
                creditCard.getOutstandingAmount();

        if (request.creditLimit().compareTo(outstandingAmount) < 0) {
            throw new BadRequestException(
                    "Credit limit cannot be lower than the outstanding amount"
            );
        }

        
        creditCard.setCardType(request.cardType());
        creditCard.setCreditLimit(request.creditLimit());
        creditCard.setExpiryDate(request.expiryDate());
        creditCard.setCardStatus(request.cardStatus());

        creditCard.setAvailableCredit(
                request.creditLimit().subtract(outstandingAmount)
        );
    }
    
    private void validateCustomerIdUnchanged(
            Long requestedCustomerId,
            Long existingCustomerId
    ) {
        if (requestedCustomerId != null
                && !requestedCustomerId.equals(existingCustomerId)) {

            throw new BadRequestException(
                    "Customer ID cannot be changed for an existing card"
            );
        }
    }

    // Convert entity into DTO
    private CreditCardDto toDto(CreditCard creditCard) {

        return new CreditCardDto(
                creditCard.getCardNumber(),
                creditCard.getCustomerId(),
                creditCard.getCardType(),
                creditCard.getCreditLimit(),
                creditCard.getAvailableCredit(),
                creditCard.getOutstandingAmount(),
                creditCard.getExpiryDate(),
                creditCard.getCardStatus()
        );
    }
    
    @Override
    public CardBalanceResponse debitCard(
            String cardNumber,
            BigDecimal amount
    ) {
        validateFinancialAmount(amount);

        CreditCard creditCard = findCardByNumber(cardNumber);

        if (creditCard.getCardStatus() != CardStatus.ACTIVE) {
            throw new BadRequestException(
                    "Credit card is not active"
            );
        }

        if (creditCard.getAvailableCredit().compareTo(amount) < 0) {
            throw new BadRequestException(
                    "Insufficient available credit"
            );
        }

        creditCard.setAvailableCredit(
                creditCard.getAvailableCredit().subtract(amount)
        );

        creditCard.setOutstandingAmount(
                creditCard.getOutstandingAmount().add(amount)
        );

        CreditCard updatedCard =
                creditCardRepository.saveAndFlush(creditCard);

        return toCardBalanceResponse(updatedCard);
    }

    @Override
    public CardBalanceResponse applyPayment(
            String cardNumber,
            BigDecimal amount
    ) {
        validateFinancialAmount(amount);

        CreditCard creditCard = findCardByNumber(cardNumber);

        if (creditCard.getOutstandingAmount().compareTo(amount) < 0) {
            throw new BadRequestException(
                    "Payment amount cannot exceed the outstanding amount"
            );
        }

        creditCard.setOutstandingAmount(
                creditCard.getOutstandingAmount().subtract(amount)
        );

        creditCard.setAvailableCredit(
                creditCard.getAvailableCredit().add(amount)
        );

        CreditCard updatedCard =
                creditCardRepository.saveAndFlush(creditCard);

        return toCardBalanceResponse(updatedCard);
    }

    private void validateFinancialAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new BadRequestException(
                    "Amount must be greater than zero"
            );
        }
    }

    private CardBalanceResponse toCardBalanceResponse(
            CreditCard creditCard
    ) {
        return new CardBalanceResponse(
                creditCard.getCardNumber(),
                creditCard.getCustomerId(),
                creditCard.getAvailableCredit(),
                creditCard.getOutstandingAmount()
        );
    }
}
