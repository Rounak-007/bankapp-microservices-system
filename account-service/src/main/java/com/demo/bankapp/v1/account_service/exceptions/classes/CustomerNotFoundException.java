package com.demo.bankapp.v1.account_service.exceptions.classes;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(
            String message) {
        super(message);
    }
}