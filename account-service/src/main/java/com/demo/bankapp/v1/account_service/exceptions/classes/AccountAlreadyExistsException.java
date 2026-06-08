package com.demo.bankapp.v1.account_service.exceptions.classes;

public class AccountAlreadyExistsException extends RuntimeException{

    public AccountAlreadyExistsException(String msg){
        super(msg);
    }
}
