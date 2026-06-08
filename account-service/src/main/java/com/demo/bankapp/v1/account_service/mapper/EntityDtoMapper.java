package com.demo.bankapp.v1.account_service.mapper;

import com.demo.bankapp.v1.account_service.dto.AccountRequestDto;
import com.demo.bankapp.v1.account_service.dto.AccountResponseDto;
import com.demo.bankapp.v1.account_service.model.Account;
import org.springframework.stereotype.Component;

@Component
public class EntityDtoMapper {

    public Account getEntityFromAccountRequestDto(AccountRequestDto dto) {
        Account account = new Account();

        account.setAccountType(dto.accountType());
        account.setCustomerId(dto.customerId());
        account.setBalance(dto.balance());

        return account;
    }


    public AccountResponseDto getAccountResponseDtoFromEntity(Account account) {

        return new AccountResponseDto(
                account.getAccountNumber(),
                account.getCustomerId(),
                account.getAccountType(),
                account.getBalance(),
                account.getStatus(),
                account.getCreatedAt(),
                account.getUpdatedAt());
    }
}
