package com.bankapp.v1.customer_service.dto;

import jakarta.validation.constraints.*;

public record CustomerRequestDto(

        @NotBlank(message = "Customer name is required")
        @Size(max = 100)
        String name,

        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^[0-9]{10}$",
                message = "Phone number must contain exactly 10 digits"
        )
        String phoneNumber,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email
) {
}