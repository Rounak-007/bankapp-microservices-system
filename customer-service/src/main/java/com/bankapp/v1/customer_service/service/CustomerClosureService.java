package com.bankapp.v1.customer_service.service;

import com.bankapp.v1.customer_service.dto.CustomerResponseDto;
import com.bankapp.v1.customer_service.model.Customer;
import com.bankapp.v1.customer_service.model.CustomerStatus;

import java.time.LocalDateTime;

public interface CustomerClosureService {

    CustomerResponseDto closeCustomerRecord(String customerId);

    void deleteCustomerAndAccount(String customerId);

//    CustomerResponseDto reopenCustomer(String customerId);
//
//    void validateCustomerState(Customer customer);
//
//    Customer markCustomerPendingClosure(Customer customer);

    //  Customer markCustomerClosed(Customer customer);

}
