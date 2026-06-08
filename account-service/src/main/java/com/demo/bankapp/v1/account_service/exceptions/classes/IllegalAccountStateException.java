package com.demo.bankapp.v1.account_service.exceptions.classes;

public class IllegalAccountStateException extends RuntimeException {
    public IllegalAccountStateException(String msg) {
        super(msg);
    }
}
