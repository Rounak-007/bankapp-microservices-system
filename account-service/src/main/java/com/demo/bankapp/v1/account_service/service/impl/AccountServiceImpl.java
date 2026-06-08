package com.demo.bankapp.v1.account_service.service.impl;

import com.demo.bankapp.v1.account_service.dto.AccountRequestDto;
import com.demo.bankapp.v1.account_service.dto.AccountResponseDto;
import com.demo.bankapp.v1.account_service.exceptions.classes.AccountAlreadyExistsException;
import com.demo.bankapp.v1.account_service.exceptions.classes.AccountNotExistException;
import com.demo.bankapp.v1.account_service.exceptions.classes.IllegalAccountStateException;
import com.demo.bankapp.v1.account_service.factory.AccountCreationFactory;
import com.demo.bankapp.v1.account_service.feignClient.CustomerClient;
import com.demo.bankapp.v1.account_service.mapper.EntityDtoMapper;
import com.demo.bankapp.v1.account_service.model.Account;
import com.demo.bankapp.v1.account_service.model.AccountStatus;
import com.demo.bankapp.v1.account_service.model.AccountType;
import com.demo.bankapp.v1.account_service.repository.AccountRepository;
import com.demo.bankapp.v1.account_service.service.AccountService;
import com.demo.bankapp.v1.account_service.strategy.AccountStrategyInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountCreationFactory accountCreationFactory;
    private final AccountRepository accountRepository;
    private final EntityDtoMapper entityDtoMapper;
    private final CustomerClient customerClient;

    public AccountServiceImpl(AccountCreationFactory accountCreationFactory, AccountRepository accountRepository, EntityDtoMapper entityDtoMapper, CustomerClient customerClient) {
        this.accountCreationFactory = accountCreationFactory;
        this.accountRepository = accountRepository;
        this.entityDtoMapper = entityDtoMapper;
        this.customerClient = customerClient;
    }

    @Override
    @Transactional
    public AccountResponseDto createAccount(AccountRequestDto request) {

        logger.info(
                "Creating {} account for customerID {}",
                request.accountType(),
                request.customerId()
        );

        checkIfAccountExists(request.customerId(), request.accountType());

        customerClient.getCustomerDetailsFromCustomerService(request.customerId());

        AccountStrategyInterface strategy = accountCreationFactory
                .getStrategy(request.accountType());

        Account newAccount = strategy.createAccount(request);

        newAccount.setAccountNumber(generateAccountNumber());

        Account savedAccount = accountRepository.save(newAccount);

        return entityDtoMapper.getAccountResponseDtoFromEntity(savedAccount);
    }

    @Override
    public AccountResponseDto getAccountByAccountNumber(String accountNumber) {
        Account account = getByAccountNumber(accountNumber);
        return entityDtoMapper.getAccountResponseDtoFromEntity(account);
    }

    @Override
    public AccountResponseDto getAccountByAccountId(Long accountId) {
        Account account = getByAccountId(accountId);
        return entityDtoMapper.getAccountResponseDtoFromEntity(account);
    }

    @Override
    public List<AccountResponseDto> getAccountsByCustomerId(String customerId) {
        List<Account> accountList = accountRepository.findAllByCustomerId(customerId);
        return accountList
                .stream()
                .map(entityDtoMapper::getAccountResponseDtoFromEntity)
                .toList();
    }

    @Override
    @Transactional
    public AccountResponseDto blockAccount(String accountNumber) {
        Account account = getByAccountNumber(accountNumber);
        validateTransition(account.getStatus(), AccountStatus.BLOCKED);
        account.setStatus(AccountStatus.BLOCKED);
        //accountRepository.save(account);
        logger.info(
                "Account with accountNumber: {} blocked",
                accountNumber
        );
        return entityDtoMapper.getAccountResponseDtoFromEntity(account);
    }

    @Override
    @Transactional
    public AccountResponseDto activateAccount(String accountNumber) {
        Account account = getByAccountNumber(accountNumber);
        validateTransition(account.getStatus(), AccountStatus.ACTIVE);
        account.setStatus(AccountStatus.ACTIVE);
        //accountRepository.save(account);
        logger.info(
                "Account with accountNumber: {} activated",
                accountNumber
        );
        return entityDtoMapper.getAccountResponseDtoFromEntity(account);
    }

    @Override
    @Transactional
    public AccountResponseDto closeAccount(String accountNumber) {
        Account account = getByAccountNumber(accountNumber);
        validateTransition(account.getStatus(), AccountStatus.CLOSED);
        account.setStatus(AccountStatus.CLOSED);
        //accountRepository.save(account);
        logger.info(
                "Account with accountNumber: {} closed",
                accountNumber
        );
        return entityDtoMapper.getAccountResponseDtoFromEntity(account);
    }

    private void checkIfAccountExists(String customerId, AccountType accountType) {
        List<AccountStatus> occupiedStatuses = List.of(AccountStatus.ACTIVE, AccountStatus.BLOCKED);

        if (accountRepository.existsByCustomerIdAndAccountTypeAndStatusIn(customerId, accountType, occupiedStatuses)) {
            throw new AccountAlreadyExistsException(String.format(
                    "CustomerID %s already has an active or blocked %s account.", customerId, accountType)
            );
        }

    }

    private String generateAccountNumber() {
        return "ACCT-" + UUID.randomUUID().toString().substring(0, 8);
    }

    private void validateTransition(AccountStatus currentStatus, AccountStatus targetStatus) {

        // throw err against identical transitions (e.g., ACTIVE -> ACTIVE)
        if (currentStatus == targetStatus) {
            throw new IllegalAccountStateException(
                    String.format("Account is already %s", currentStatus.name().toLowerCase())
            );
        }

        // Validate if the transition is legally allowed
        if (!currentStatus.canTransitionTo(targetStatus)) {
            throw new IllegalAccountStateException(
                    String.format("Transaction rejected: Cannot change account status from %s to %s",
                            currentStatus, targetStatus)
            );
        }
    }

    private Account getByAccountNumber(String accountNumber) {

        return accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotExistException("Account not found: " + accountNumber));
    }

    private Account getByAccountId(Long accountId) {

        return accountRepository
                .findById(accountId)
                .orElseThrow(() -> new AccountNotExistException("Account not found: " + accountId));
    }
}
