package com.demo.bankapp.v1.account_service.strategy.impl;

import com.demo.bankapp.v1.account_service.dto.AccountRequestDto;
import com.demo.bankapp.v1.account_service.model.Account;
import com.demo.bankapp.v1.account_service.model.AccountStatus;
import com.demo.bankapp.v1.account_service.model.AccountType;
import com.demo.bankapp.v1.account_service.strategy.AccountStrategyInterface;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrentAccountStrategyImpl implements AccountStrategyInterface {
    @Override
    public AccountType getAccountType() {
        return AccountType.CURRENT;
    }

    @Override
    public Account createAccount(AccountRequestDto request) {
        Account account = new Account();

        account.setAccountType(AccountType.CURRENT);
        account.setBalance(BigDecimal.valueOf(10000));
        account.setStatus(AccountStatus.ACTIVE);
        account.setCustomerId(request.customerId());
        return account;
    }
}
