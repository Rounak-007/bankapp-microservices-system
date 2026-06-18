package com.bankapp.v1.customer_service.service.impl;

import com.bankapp.v1.customer_service.dto.AccountResponseDto;
import com.bankapp.v1.customer_service.dto.CustomerProfileDetailsDto;
import com.bankapp.v1.customer_service.dto.CustomerResponseDto;
import com.bankapp.v1.customer_service.feignclient.AccountClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AccountClientService {

    private final AccountClient accountFeignClient;
    private static final Logger logger = LoggerFactory.getLogger(AccountClientService.class);

    public AccountClientService(AccountClient accountFeignClient) {
        this.accountFeignClient = accountFeignClient;
    }

    @Retry(name = "account-service-retry")
    @CircuitBreaker(name = "account-service-circuit-breaker", fallbackMethod = "fallback")
    public CustomerProfileDetailsDto getAccounts(CustomerResponseDto customer, String customerId) {
        List<AccountResponseDto> accounts= accountFeignClient.getAccountsByCustomerId(customerId);
        return new CustomerProfileDetailsDto(customer,accounts,"ACTIVE");
    }

    public CustomerProfileDetailsDto fallback(CustomerResponseDto customer, String customerId, Throwable t) {
        System.err.println("inside fallback method for circuit-breaker...cause.."+t.getCause().getMessage());
        System.err.println("Account service fallback triggered due to: " + t.getMessage());
        logger.error("Account Service call failed: {}", t.getMessage(), t);
        return new CustomerProfileDetailsDto(customer,Collections.emptyList(),t.getMessage());
    }

}
