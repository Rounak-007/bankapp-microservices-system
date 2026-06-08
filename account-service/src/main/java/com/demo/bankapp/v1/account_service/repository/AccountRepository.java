package com.demo.bankapp.v1.account_service.repository;

import com.demo.bankapp.v1.account_service.model.AccountStatus;
import com.demo.bankapp.v1.account_service.model.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.bankapp.v1.account_service.model.Account;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByCustomerId(String customerId);

    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findAllByCustomerId(String customerId);

    boolean existsByCustomerIdAndAccountType(String customerId, AccountType accountType);

    // Finds if an active/blocked account of this type already exists for the customer
    boolean existsByCustomerIdAndAccountTypeAndStatusIn(
            String customerId, AccountType accountType, List<AccountStatus> status
    );
}
