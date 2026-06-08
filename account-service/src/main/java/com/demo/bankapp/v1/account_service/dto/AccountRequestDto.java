package com.demo.bankapp.v1.account_service.dto;

import com.demo.bankapp.v1.account_service.model.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AccountRequestDto(
        @NotBlank(message = "Customer ID is required")
        String customerId,

        @NotNull(message = "Please select a Account Type")
        AccountType accountType,

        BigDecimal balance) {
}
