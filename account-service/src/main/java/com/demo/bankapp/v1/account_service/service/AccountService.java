package com.demo.bankapp.v1.account_service.service;

import com.demo.bankapp.v1.account_service.dto.AccountRequestDto;
import com.demo.bankapp.v1.account_service.dto.AccountResponseDto;

import java.util.List;

public interface AccountService {

    AccountResponseDto createAccount(AccountRequestDto request);

    AccountResponseDto getAccountByAccountNumber(String accountNumber);

    AccountResponseDto getAccountByAccountId(Long accountId);

    List<AccountResponseDto> getAccountsByCustomerId(String customerId);

    AccountResponseDto blockAccount(String accountNumber);

    AccountResponseDto activateAccount(String accountNumber);

    AccountResponseDto closeAccount(String accountNumber);
}