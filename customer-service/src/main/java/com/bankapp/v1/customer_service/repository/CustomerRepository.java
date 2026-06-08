package com.bankapp.v1.customer_service.repository;

import com.bankapp.v1.customer_service.model.CustomerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bankapp.v1.customer_service.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    Optional<Customer> findByCustomerId(String customerId);

    List<Customer> findByStatus(CustomerStatus status);
}
