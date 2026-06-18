package com.bankapp.v1.customer_service.dto;

import java.util.List;

public record CustomerProfileDetailsDto(
        CustomerResponseDto customer,
        List<AccountResponseDto> accounts,
        String accountServiceStatus
) {
}