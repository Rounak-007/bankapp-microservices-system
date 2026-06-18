package com.bankapp.v1.api_gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallBackController {

    @RequestMapping("/account-service")
    public ResponseEntity<Map<String, String>> accountFallback() {
//        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
//                .body("Account Service is taking too long to respond or is down. Please try again later.");

        Map<String, String> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.SERVICE_UNAVAILABLE.toString());
        body.put("error", "Service Unavailable");
        body.put("message", "The Account system is currently undergoing maintenance. Please try again later.");
        body.put("source", "api-gateway-fallback");

        return new ResponseEntity<>(body, HttpStatus.SERVICE_UNAVAILABLE);
    }

    //@GetMapping("/customer-service")
    @RequestMapping("/customer-service")
    public ResponseEntity<String> customerFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Customer Service is currently unavailable.");
    }
}



