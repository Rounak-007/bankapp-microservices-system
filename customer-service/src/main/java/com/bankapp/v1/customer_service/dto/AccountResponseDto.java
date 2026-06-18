package com.bankapp.v1.customer_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountResponseDto(String accountNumber,
                                 String customerId,
                                 String accountType,
                                 BigDecimal balance,
                                 String status,
                                 LocalDateTime createdAt,
                                 LocalDateTime updatedAt) {
}
