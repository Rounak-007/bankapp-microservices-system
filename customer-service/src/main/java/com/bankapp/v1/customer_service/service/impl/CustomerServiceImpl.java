package com.bankapp.v1.customer_service.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.bankapp.v1.customer_service.dto.CustomerRequestDto;
import com.bankapp.v1.customer_service.dto.CustomerResponseDto;
import com.bankapp.v1.customer_service.exceptions.DuplicateEmailException;
import com.bankapp.v1.customer_service.exceptions.DuplicatePhoneNumberException;
import com.bankapp.v1.customer_service.feignclient.AccountClient;
import com.bankapp.v1.customer_service.mapper.EntityDtoMapper;
import com.bankapp.v1.customer_service.model.CustomerStatus;
import com.bankapp.v1.customer_service.service.CustomerService;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

//import com.nagarro.bankapp.customer_service.feignclient.AccountClient;
import com.bankapp.v1.customer_service.model.Customer;
import com.bankapp.v1.customer_service.repository.CustomerRepository;
import com.bankapp.v1.customer_service.exceptions.CustomerNotExistException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;
    private final EntityDtoMapper entityDtoMapper;
    private final AccountClient accountClient;

    public CustomerServiceImpl(CustomerRepository customerRepository, EntityDtoMapper entityDtoMapper, AccountClient accountClient) {
        this.customerRepository = customerRepository;
        this.entityDtoMapper = entityDtoMapper;
        this.accountClient = accountClient;
    }

    @Override
    @Transactional
    public CustomerResponseDto createCustomer(CustomerRequestDto customerRequestDto) {
        checkIfDuplicatePhoneNumberExists(customerRequestDto.phoneNumber());
        checkIfDuplicateEmailExists(customerRequestDto.email());

        Customer newCustomer = entityDtoMapper.getEntityFromRequestDto(customerRequestDto);

        newCustomer.setCustomerId(generateCustomerId());
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
        Customer existingCustomer = getCustomerEntityByCustomerId(customerId);
        return entityDtoMapper.getResponseDtoFromEntity(existingCustomer);
    }

    @Override
    @Transactional
    public CustomerResponseDto updateCustomer(String customerId, CustomerRequestDto updatedCustomer) {
        Customer existingCustomer = getCustomerEntityByCustomerId(customerId);
        existingCustomer.setName(updatedCustomer.name());
        existingCustomer.setPhoneNumber(updatedCustomer.phoneNumber());
        existingCustomer.setEmail(updatedCustomer.email());
        existingCustomer.setUpdatedAt(LocalDateTime.now());
        return entityDtoMapper.getResponseDtoFromEntity(customerRepository.save(existingCustomer));
    }

    @Override
    @Transactional
    public void deleteCustomer(String customerId) {
        Customer customer = getCustomerEntityByCustomerId(customerId);

        logger.info("Deleting customer with id {}", customerId);

        customerRepository.delete(customer);
    }

    @Override
    public void deleteCustomerAndAccount(String customerId) {

    }

    @Override
    public CustomerResponseDto closeCustomerRecord(String customerId) {

        Customer existingCustomer = getCustomerEntityByCustomerId(customerId);
        validateCustomerState(existingCustomer);
        try {

            // STEP 1-a: KYC validation
            // boolean valid = kycClient.validateCustomer(customerId);
            // if (!valid) throw new RuntimeException("KYC failed");

            // STEP 1-b: customer status setting as "PENDING_CLOSURE"
            Customer customerInPendingClosureState = markCustomerPendingClosure(existingCustomer);
            logger.info("Customer updated to PENDING-CLOSURE, customer details: {}", customerInPendingClosureState);

            int a = 90 / 0;
            // STEP 2: close account
            //accountClient.closeAccount(customerId);

            // STEP 3: notify
            //notificationClient.sendNotification("Account closed for " + customerId);

            // STEP-4: mark customer as CLOSED and return a response
            Customer customerInClosedState = markCustomerClosed(customerInPendingClosureState);
            logger.info("Customer Record CLOSED, customer details: {}", customerInClosedState);
            return entityDtoMapper.getResponseDtoFromEntity(customerInClosedState);

        } catch (FeignException e) {
            // TODO: handle exception
            compensate(customerId);
            logger.error("An ERROR occurred with FeignClient during Customer Closure with Customer ID: {}", customerId, e);
            throw e;
        } catch (Exception ex) {
            compensate(customerId);
            logger.error("Customer Closure FAILED for customerID: {}", customerId);
            throw new RuntimeException("Customer closure failed", ex);
        }
    }

    private void validateCustomerState(Customer customer) {

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

    @Scheduled(fixedDelay = 60000)
    public void recoverPendingClosures() {

        logger.info("Recovery function started.....");

        List<Customer> customers = customerRepository.findByStatus(CustomerStatus.PENDING_CLOSURE);

        if (customers.isEmpty()) {
            return;
        }

        logger.info("Recovering {} pending customer closures", customers.size());

        for (Customer customer : customers) {

            try {
                //accountClient.closeAccount(customer.getCustomerId());
                markCustomerClosed(customer);
                logger.info("Recovered closure for customer = {}", customer.getCustomerId());
            } catch (Exception ex) {
                logger.error(
                        "Recovery failed for customer={}",
                        customer.getCustomerId(),
                        ex);
            }
        }
    }

    private Customer markCustomerPendingClosure(Customer customer) {
        customer.setStatus(CustomerStatus.PENDING_CLOSURE);
        customer.setUpdatedAt(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    private Customer markCustomerClosed(Customer customer) {
        customer.setStatus(CustomerStatus.CLOSED);
        customer.setUpdatedAt(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    private Customer getCustomerEntityByCustomerId(String customerId) {
        return customerRepository.findByCustomerId(customerId)
                .orElseThrow(() ->
                        new CustomerNotExistException(
                                "Customer not found with ID: " + customerId));
    }

    private void checkIfDuplicatePhoneNumberExists(String phoneNumber) {
        if (customerRepository.existsByPhoneNumber(phoneNumber)) {

            logger.error("Phone number already registered!!");
            throw new DuplicatePhoneNumberException(
                    "Phone number already registered!!");
        }
    }

    private void checkIfDuplicateEmailExists(String email) {
        if (customerRepository.existsByEmail(email)) {
            logger.error("Email already registered!!");
            throw new DuplicateEmailException(
                    "Email already registered!!");
        }
    }

    private String generateCustomerId() {
        return "CUST-" + UUID.randomUUID().toString().substring(0, 8);
    }

    private void compensate(String customerId) {

        try {
            // rollback account state
            //accountClient.rollbackAccount(customerId);

            // reopen customer if partially closed
            reopenCustomer(customerId);

        } catch (Exception e) {
            logger.error(
                    "COMPENSATION FAILED for customerID: {}",
                    customerId,
                    e
            );
        }
    }

    @Override
    public CustomerResponseDto reopenCustomer(String customerId) {
        Customer customer=getCustomerEntityByCustomerId(customerId);
        customer.setStatus(CustomerStatus.ACTIVE);
        customer.setUpdatedAt(LocalDateTime.now());
        return entityDtoMapper.getResponseDtoFromEntity(customerRepository.save(customer));
    }

}
