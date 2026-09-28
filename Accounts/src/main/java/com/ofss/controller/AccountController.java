package com.ofss.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ofss.dto.AccountDto;
import com.ofss.service.AccountService;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // POST /api/accounts
    @PostMapping
    public ResponseEntity<AccountDto> createAccount(
            @Valid @RequestBody AccountDto request
    ) {

        AccountDto account = accountService.createAccount(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(account);
    }

    // GET /api/accounts/{accountId}
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDto> getAccountById(
            @PathVariable("accountId") Long accountId
    ) {

        AccountDto account = accountService.getAccountById(accountId);

        return ResponseEntity.ok(account);
    }

    // GET /api/accounts
    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {

        List<AccountDto> accounts = accountService.getAllAccounts();

        return ResponseEntity.ok(accounts);
    }

    // GET /api/accounts/customer/{customerId}
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountDto>> getAccountsByCustomerId(
            @PathVariable("customerId") Long customerId
    ) {

        List<AccountDto> accounts =
                accountService.getAccountsByCustomerId(customerId);

        return ResponseEntity.ok(accounts);
    }

    // PUT /api/accounts/{accountId}
    @PutMapping("/{accountId}")
    public ResponseEntity<AccountDto> updateAccount(
            @Valid @RequestBody AccountDto request,
            @PathVariable("accountId") Long accountId
    ) {

        AccountDto account = accountService.updateAccount(
                request,
                accountId
        );

        return ResponseEntity.ok(account);
    }

    // PATCH /api/accounts/{accountId}
    @PatchMapping("/{accountId}")
    public ResponseEntity<AccountDto> patchAccount(
            @RequestBody AccountDto request,
            @PathVariable("accountId") Long accountId
    ) {

        AccountDto account = accountService.patchAccount(
                request,
                accountId
        );

        return ResponseEntity.ok(account);
    }

    // DELETE /api/accounts/{accountId}
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(
            @PathVariable("accountId") Long accountId
    ) {

        accountService.deleteAccount(accountId);

        return ResponseEntity.noContent().build();
    }
}
