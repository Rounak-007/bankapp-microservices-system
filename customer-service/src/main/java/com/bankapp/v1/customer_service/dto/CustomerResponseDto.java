package com.bankapp.v1.customer_service.dto;

import com.bankapp.v1.customer_service.model.CustomerStatus;

public record CustomerResponseDto(String customerId,
                                  String name, String phoneNumber, String email,
                                  CustomerStatus status) {
}
