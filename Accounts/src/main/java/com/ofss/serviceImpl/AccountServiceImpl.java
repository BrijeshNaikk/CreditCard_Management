package com.ofss.serviceImpl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ofss.dto.AccountDto;
import com.ofss.entity.Account;
import com.ofss.exception.BadRequestException;
import com.ofss.exception.ResourceNotFoundException;
import com.ofss.repository.AccountRepository;
import com.ofss.service.AccountService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final Validator validator;

    public AccountServiceImpl(
            AccountRepository accountRepository,
            Validator validator
    ) {
        this.accountRepository = accountRepository;
        this.validator = validator;
    }

    // POST: Create an account
    @Override
    public AccountDto createAccount(AccountDto request) {

        validateAccountData(request);

        Account account = new Account();

        copyFields(request, account);

        Account savedAccount = accountRepository.save(account);

        return toDto(savedAccount);
    }

    // GET: Retrieve one account
    @Override
    @Transactional(readOnly = true)
    public AccountDto getAccountById(Long accountId) {

        Account account = findAccountById(accountId);

        return toDto(account);
    }

    // GET: Retrieve all accounts
    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> getAllAccounts() {

        return accountRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    // GET: Retrieve accounts associated with a customer ID
    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> getAccountsByCustomerId(Long customerId) {

        validateId(customerId, "Customer ID");

        return accountRepository.findByCustomerId(customerId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    // PUT: Replace all writable account fields
    @Override
    public AccountDto updateAccount(
            AccountDto request,
            Long accountId
    ) {

        Account account = findAccountById(accountId);

        validateAccountData(request);

        copyFields(request, account);

        Account updatedAccount = accountRepository.save(account);

        return toDto(updatedAccount);
    }

    // PATCH: Update only supplied non-null fields
    @Override
    public AccountDto patchAccount(
            AccountDto request,
            Long accountId
    ) {

        if (request == null) {
            throw new BadRequestException("Request body is required");
        }

        Account account = findAccountById(accountId);

        AccountDto mergedRequest = new AccountDto(
                account.getAccountId(),

                request.accountNumber() != null
                        ? request.accountNumber()
                        : account.getAccountNumber(),

                request.accountType() != null
                        ? request.accountType()
                        : account.getAccountType(),

                request.balance() != null
                        ? request.balance()
                        : account.getBalance(),

                request.customerId() != null
                        ? request.customerId()
                        : account.getCustomerId()
        );

        validateAccountData(mergedRequest);

        copyFields(mergedRequest, account);

        Account updatedAccount = accountRepository.save(account);

        return toDto(updatedAccount);
    }

    // DELETE: Permanently delete an account
    @Override
    public void deleteAccount(Long accountId) {

        Account account = findAccountById(accountId);

        accountRepository.delete(account);
    }

    // Find an account or throw an exception
    private Account findAccountById(Long accountId) {

        validateId(accountId, "Account ID");

        return accountRepository.findById(accountId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Account",
                                accountId.toString()
                        )
                );
    }

    // Validate account and customer IDs
    private void validateId(Long id, String fieldName) {

        if (id == null || id <= 0) {
            throw new BadRequestException(
                    fieldName + " must be a positive number"
            );
        }
    }

    // Check the validation annotations in AccountDto
    private void validateAccountData(AccountDto request) {

        if (request == null) {
            throw new BadRequestException("Request body is required");
        }

        Set<ConstraintViolation<AccountDto>> violations =
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

    // Copy writable fields into the entity
    private void copyFields(AccountDto request, Account account) {

        account.setAccountNumber(request.accountNumber());
        account.setAccountType(request.accountType());
        account.setBalance(request.balance());
        account.setCustomerId(request.customerId());
    }

    // Convert entity into DTO
    private AccountDto toDto(Account account) {

        return new AccountDto(
                account.getAccountId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getBalance(),
                account.getCustomerId()
        );
    }
}
