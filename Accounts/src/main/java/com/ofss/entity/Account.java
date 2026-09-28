package com.ofss.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_id_generator"
    )
    @SequenceGenerator(
            name = "account_id_generator",
            sequenceName = "accounts_seq",
            allocationSize = 1
    )
    @Column(name = "account_id")
    private Long accountId;

    @Column(
            name = "account_number",
            nullable = false,
            unique = true,
            length = 20
    )
    private String accountNumber;

    @Column(
            name = "account_type",
            nullable = false,
            length = 20
    )
    private String accountType;

    @Column(
            name = "balance",
            nullable = false,
            precision = 15,
            scale = 2
    )
    private BigDecimal balance;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    public Account() {
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}