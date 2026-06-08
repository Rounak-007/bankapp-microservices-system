package com.demo.bankapp.v1.account_service.dto;

import com.demo.bankapp.v1.account_service.model.AccountStatus;
import com.demo.bankapp.v1.account_service.model.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountResponseDto(String accountNumber,
                                 String customerId,
                                 AccountType accountType,
                                 BigDecimal balance,
                                 AccountStatus status,
                                 LocalDateTime createdAt,
                                 LocalDateTime updatedAt) {
}
