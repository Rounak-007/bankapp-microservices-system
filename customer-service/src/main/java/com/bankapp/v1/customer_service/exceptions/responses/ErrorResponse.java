package com.bankapp.v1.customer_service.exceptions.responses;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        String message,
        Map<String, String> errors,
        int status,
        LocalDateTime timestamp) {
}

