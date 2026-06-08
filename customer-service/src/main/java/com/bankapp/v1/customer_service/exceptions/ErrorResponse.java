package com.bankapp.v1.customer_service.exceptions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ErrorResponse(
        String globalMessage,        // E.g., "Validation failed for the request"
        Map<String, String> errors,
        int status,
        LocalDateTime timestamp) {
}

