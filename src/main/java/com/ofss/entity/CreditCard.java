package com.ofss.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ofss.enums.CardStatus;
import com.ofss.enums.CardType;

@Entity
@Table(name = "credit_cards")
public class CreditCard {

    @Id
    @Column(
            name = "card_number",
            nullable = false,
            updatable = false,
            length = 19
    )
    private String cardNumber;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "card_type",
            nullable = false,
            length = 20
    )
    private CardType cardType;

    @Column(
            name = "credit_limit",
            nullable = false,
            precision = 15,
            scale = 2
    )
    private BigDecimal creditLimit;

    @Column(
            name = "available_credit",
            nullable = false,
            precision = 15,
            scale = 2
    )
    private BigDecimal availableCredit;

    @Column(
            name = "outstanding_amount",
            nullable = false,
            precision = 15,
            scale = 2
    )
    private BigDecimal outstandingAmount;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "card_status",
            nullable = false,
            length = 20
    )
    private CardStatus cardStatus;
    
    @Version
    @Column(name = "version")
    private Long version;

    public CreditCard() {
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getAvailableCredit() {
        return availableCredit;
    }

    public void setAvailableCredit(BigDecimal availableCredit) {
        this.availableCredit = availableCredit;
    }

    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public CardStatus getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(CardStatus cardStatus) {
        this.cardStatus = cardStatus;
    }
}