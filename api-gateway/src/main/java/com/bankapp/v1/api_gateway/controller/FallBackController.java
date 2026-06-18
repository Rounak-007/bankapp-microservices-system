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
        Map<String, String> accountFallBackBody = new HashMap<>();
        accountFallBackBody.put("timestamp", LocalDateTime.now().toString());
        accountFallBackBody.put("status", HttpStatus.SERVICE_UNAVAILABLE.toString());
        accountFallBackBody.put("error", "Service Unavailable");
        accountFallBackBody.put("message", "The Account system is currently undergoing maintenance. Please try again later.");
        accountFallBackBody.put("source", "api-gateway-fallback");

        return new ResponseEntity<>(accountFallBackBody, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @RequestMapping("/customer-service")
    public ResponseEntity<Map<String, String>> customerFallback() {
        HashMap<String, String> customerFallBackBody = new HashMap<>();
        customerFallBackBody.put("timestamp", LocalDateTime.now().toString());
        customerFallBackBody.put("status", HttpStatus.SERVICE_UNAVAILABLE.toString());
        customerFallBackBody.put("error", "Service Unavailable");
        customerFallBackBody.put("message", "The Account system is currently undergoing maintenance. Please try again later.");
        customerFallBackBody.put("source", "api-gateway-fallback");

        return new ResponseEntity<>(customerFallBackBody, HttpStatus.SERVICE_UNAVAILABLE);
    }
}



