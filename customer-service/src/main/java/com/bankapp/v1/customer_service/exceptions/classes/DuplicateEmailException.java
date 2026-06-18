package com.bankapp.v1.customer_service.exceptions.classes;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String msg) {
        super(msg);
    }
}
