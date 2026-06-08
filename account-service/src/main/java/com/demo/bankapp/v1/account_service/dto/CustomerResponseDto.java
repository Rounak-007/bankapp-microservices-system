package com.demo.bankapp.v1.account_service.dto;

public record CustomerResponseDto(String customerId, String name, String phoneNumber, String email,
                                  String status) {
}
