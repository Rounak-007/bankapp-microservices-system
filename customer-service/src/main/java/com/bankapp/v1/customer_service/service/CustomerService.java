package com.bankapp.v1.customer_service.service;

import java.util.List;

import com.bankapp.v1.customer_service.dto.AccountResponseDto;
import com.bankapp.v1.customer_service.dto.CustomerProfileDetailsDto;
import com.bankapp.v1.customer_service.dto.CustomerRequestDto;
import com.bankapp.v1.customer_service.dto.CustomerResponseDto;
import com.bankapp.v1.customer_service.model.Customer;

public interface CustomerService {

    CustomerResponseDto createCustomer(CustomerRequestDto customerRequestDto);

    CustomerResponseDto createCustomerWithAccount(CustomerRequestDto customerRequestDto);

    List<CustomerResponseDto> getAllCustomers();

    CustomerResponseDto getCustomer(String customerId);

    CustomerResponseDto updateCustomer(String customerId, CustomerRequestDto updatedCustomer);

    void deleteCustomer(String customerId);

    CustomerProfileDetailsDto fetchAccountsAndAggregate(String customerId);
}
