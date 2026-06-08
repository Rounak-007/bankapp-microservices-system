package com.demo.bankapp.v1.account_service.exceptions.classes;

public class DownstreamServiceUnavailableException extends RuntimeException {
    public DownstreamServiceUnavailableException(String message) {
        super(message);
    }
}