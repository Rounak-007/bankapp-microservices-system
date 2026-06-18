package com.bankapp.v1.customer_service.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.bankapp.v1.customer_service.dto.CustomerProfileDetailsDto;
import com.bankapp.v1.customer_service.dto.CustomerRequestDto;
import com.bankapp.v1.customer_service.dto.CustomerResponseDto;
import com.bankapp.v1.customer_service.mapper.EntityDtoMapper;
import com.bankapp.v1.customer_service.model.CustomerStatus;
import com.bankapp.v1.customer_service.service.CustomerService;
import com.bankapp.v1.customer_service.service.helper.CustomerServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bankapp.v1.customer_service.model.Customer;
import com.bankapp.v1.customer_service.repository.CustomerRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;
    private final EntityDtoMapper entityDtoMapper;
    private final AccountClientService accountClientService;
    private final CustomerServiceHelper customerServiceHelper;

    public CustomerServiceImpl(CustomerRepository customerRepository, EntityDtoMapper entityDtoMapper, AccountClientService accountClientService, CustomerServiceHelper customerServiceHelper) {
        this.customerRepository = customerRepository;
        this.entityDtoMapper = entityDtoMapper;
        this.accountClientService = accountClientService;
        this.customerServiceHelper = customerServiceHelper;
    }

    @Override
    @Transactional
    public CustomerResponseDto createCustomer(CustomerRequestDto customerRequestDto) {
        customerServiceHelper.checkIfDuplicatePhoneNumberExists(customerRequestDto.phoneNumber());
        customerServiceHelper.checkIfDuplicateEmailExists(customerRequestDto.email());

        Customer newCustomer = entityDtoMapper.getEntityFromRequestDto(customerRequestDto);

        newCustomer.setCustomerId(customerServiceHelper.generateCustomerId());
        newCustomer.setStatus(CustomerStatus.ACTIVE);
        newCustomer.setCreatedAt(LocalDateTime.now());
        newCustomer.setUpdatedAt(LocalDateTime.now());

        Customer savedCustomer = customerRepository.save(newCustomer);

        logger.info("Customer record successfully created with customerId {}", savedCustomer.getCustomerId());

        return entityDtoMapper.getResponseDtoFromEntity(savedCustomer);
    }

    @Override
    public CustomerResponseDto createCustomerWithAccount(CustomerRequestDto customerRequestDto) {
        return null;
    }

    @Override
    public List<CustomerResponseDto> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(entityDtoMapper::getResponseDtoFromEntity)
                .toList();
    }

    @Override
    public CustomerResponseDto getCustomer(String customerId) {
        Customer existingCustomer = customerServiceHelper.getCustomerEntityByCustomerId(customerId);
        return entityDtoMapper.getResponseDtoFromEntity(existingCustomer);
    }

    @Override
    @Transactional
    public CustomerResponseDto updateCustomer(String customerId, CustomerRequestDto updatedCustomer) {
        Customer existingCustomer = customerServiceHelper.getCustomerEntityByCustomerId(customerId);
        existingCustomer.setName(updatedCustomer.name());
        existingCustomer.setPhoneNumber(updatedCustomer.phoneNumber());
        existingCustomer.setEmail(updatedCustomer.email());
        existingCustomer.setUpdatedAt(LocalDateTime.now());
        return entityDtoMapper.getResponseDtoFromEntity(customerRepository.save(existingCustomer));
    }

    @Override
    @Transactional
    public void deleteCustomer(String customerId) {
        Customer customer = customerServiceHelper.getCustomerEntityByCustomerId(customerId);

        logger.info("Deleting customer with id {}", customerId);

        customerRepository.delete(customer);
    }

    @Override
    public CustomerProfileDetailsDto fetchAccountsAndAggregate(String customerId) {
        CustomerResponseDto customer = getCustomer(customerId);
        return accountClientService.getAccounts(customer, customerId);
    }
}
