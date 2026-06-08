package com.bankapp.v1.customer_service.exceptions;

public class DuplicatePhoneNumberException extends RuntimeException {

public DuplicatePhoneNumberException(String msg){
    super(msg);
}
}
