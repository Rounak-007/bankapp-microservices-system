package com.demo.bankapp.v1.account_service.factory;

import com.demo.bankapp.v1.account_service.model.AccountType;
import com.demo.bankapp.v1.account_service.strategy.AccountStrategyInterface;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AccountCreationFactory {

    private final Map<AccountType, AccountStrategyInterface> strategies;

    public AccountCreationFactory(List<AccountStrategyInterface> strategyList) {
        this.strategies = strategyList
                .stream()
                .collect(Collectors
                        .toMap(
                                AccountStrategyInterface::getAccountType,
                                Function.identity()
                        )
                );
    }

    public AccountStrategyInterface getStrategy(AccountType accountType) {
        return Optional
                .ofNullable(strategies.get(accountType))
                .orElseThrow(() -> new IllegalArgumentException("Unsupported account type"));
    }
}
