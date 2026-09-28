package com.ofss.service;

import java.util.List;

import com.ofss.dto.AccountDto;

public interface AccountService {

	AccountDto createAccount(AccountDto request);

    AccountDto getAccountById(Long accountId);

    List<AccountDto> getAllAccounts();

    List<AccountDto> getAccountsByCustomerId(Long customerId);

    AccountDto updateAccount(AccountDto request, Long accountId);

    AccountDto patchAccount(AccountDto request, Long accountId);

    void deleteAccount(Long accountId);
}
