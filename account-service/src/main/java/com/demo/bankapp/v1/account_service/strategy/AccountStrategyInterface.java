package com.demo.bankapp.v1.account_service.strategy;

import com.demo.bankapp.v1.account_service.dto.AccountRequestDto;
import com.demo.bankapp.v1.account_service.model.Account;
import com.demo.bankapp.v1.account_service.model.AccountType;

public interface AccountStrategyInterface {

    AccountType getAccountType();
    Account createAccount(AccountRequestDto request);
}
