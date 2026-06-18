package com.bankapp.v1.customer_service.feignclient;

import com.bankapp.v1.customer_service.dto.AccountResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class AccountFallbackFactory implements FallbackFactory<AccountClient> {

    private static final Logger logger = LoggerFactory.getLogger(AccountFallbackFactory.class);

    @Override
    public AccountClient create(Throwable cause) {
        System.out.println("Account service fallback triggered due to: " + cause.getMessage());
        logger.error("Account service unavailable", cause);

        return new AccountClient() {

            @Override
            public List<AccountResponseDto> getAccountsByCustomerId(String customerId) {
                return Collections.emptyList();
            }

            @Override
            public ResponseEntity<String> deleteAccount(String accountId) {
                return null;
            }

            @Override
            public ResponseEntity<String> deleteAccountByCustomerId(String customerId) {
                return null;
            }

            @Override
            public void freezeAccount(String customerId) {

            }

            @Override
            public void closeAccount(String customerId) {

            }

            @Override
            public void rollbackAccount(String customerId) {

            }
        };
    }
}