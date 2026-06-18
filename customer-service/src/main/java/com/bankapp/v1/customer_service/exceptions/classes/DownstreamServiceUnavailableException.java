package com.bankapp.v1.customer_service.exceptions.classes;

public class DownstreamServiceUnavailableException extends RuntimeException{

    public DownstreamServiceUnavailableException(String msg){
        super(msg);
    }

    public DownstreamServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
