package com.bankapp.v1.customer_service.service.helper;

import com.bankapp.v1.customer_service.dto.CustomerResponseDto;
import com.bankapp.v1.customer_service.exceptions.classes.CustomerNotExistException;
import com.bankapp.v1.customer_service.exceptions.classes.DuplicateEmailException;
import com.bankapp.v1.customer_service.exceptions.classes.DuplicatePhoneNumberException;
import com.bankapp.v1.customer_service.mapper.EntityDtoMapper;
import com.bankapp.v1.customer_service.model.Customer;
import com.bankapp.v1.customer_service.model.CustomerStatus;
import com.bankapp.v1.customer_service.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class CustomerServiceHelper {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceHelper.class);

    private final CustomerRepository customerRepository;
    private final EntityDtoMapper entityDtoMapper;

    public CustomerServiceHelper(CustomerRepository customerRepository, EntityDtoMapper entityDtoMapper) {
        this.customerRepository = customerRepository;
        this.entityDtoMapper = entityDtoMapper;
    }

    public Customer getCustomerEntityByCustomerId(String customerId) {
        return customerRepository.findByCustomerId(customerId)
                .orElseThrow(() ->
                        new CustomerNotExistException(
                                "Customer not found with ID: " + customerId));
    }

    public void checkIfDuplicatePhoneNumberExists(String phoneNumber) {
        if (customerRepository.existsByPhoneNumber(phoneNumber)) {

            logger.error("Phone number already registered!!");
            throw new DuplicatePhoneNumberException(
                    "Phone number already registered!!");
        }
    }

    public void checkIfDuplicateEmailExists(String email) {
        if (customerRepository.existsByEmail(email)) {
            logger.error("Email already registered!!");
            throw new DuplicateEmailException(
                    "Email already registered!!");
        }
    }

    public String generateCustomerId() {
        return "CUST-" + UUID.randomUUID().toString().substring(0, 8);
    }

    @Transactional
    public CustomerResponseDto reopenCustomer(String customerId) {
        System.out.println("Reopening customer.....");
        Customer customer = getCustomerEntityByCustomerId(customerId);
        customer.setStatus(CustomerStatus.ACTIVE);
        customer.setUpdatedAt(LocalDateTime.now());
        return entityDtoMapper.getResponseDtoFromEntity(customerRepository.save(customer));
    }

    @Transactional
    public Customer markCustomerPendingClosure(Customer customer) {
        System.out.println("marking pending-closure customer.....");
        customer.setStatus(CustomerStatus.PENDING_CLOSURE);
        customer.setUpdatedAt(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer markCustomerClosed(Customer customer) {
        System.out.println("Marking close customer.....");
        customer.setStatus(CustomerStatus.CLOSED);
        customer.setUpdatedAt(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    public void validateCustomerState(Customer customer) {

        if (customer.getStatus() == CustomerStatus.CLOSED) {

            throw new RuntimeException(
                    "Customer already closed"
            );
        }

        if (customer.getStatus()
                == CustomerStatus.PENDING_CLOSURE) {

            throw new RuntimeException(
                    "Customer closure already in progress"
            );
        }
    }

}
