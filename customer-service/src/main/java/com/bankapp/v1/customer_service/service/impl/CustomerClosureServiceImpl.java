package com.bankapp.v1.customer_service.service.impl;

import com.bankapp.v1.customer_service.dto.CustomerResponseDto;
import com.bankapp.v1.customer_service.mapper.EntityDtoMapper;
import com.bankapp.v1.customer_service.model.Customer;
import com.bankapp.v1.customer_service.service.CustomerClosureService;
import com.bankapp.v1.customer_service.service.helper.CustomerServiceHelper;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CustomerClosureServiceImpl implements CustomerClosureService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerClosureServiceImpl.class);

    private final CustomerServiceHelper customerServiceHelper;
    private final EntityDtoMapper entityDtoMapper;

    public CustomerClosureServiceImpl(CustomerServiceHelper customerServiceHelper,EntityDtoMapper entityDtoMapper) {
        this.customerServiceHelper = customerServiceHelper;
        this.entityDtoMapper = entityDtoMapper;
    }

    @Override
    public CustomerResponseDto closeCustomerRecord(String customerId) {

        Customer existingCustomer = customerServiceHelper.getCustomerEntityByCustomerId(customerId);
        customerServiceHelper.validateCustomerState(existingCustomer);
        try {

            // STEP 1-a: KYC validation
            // boolean valid = kycClient.validateCustomer(customerId);
            // if (!valid) throw new RuntimeException("KYC failed");

            // STEP 1-b: customer status setting as "PENDING_CLOSURE"
            Customer customerInPendingClosureState = customerServiceHelper.markCustomerPendingClosure(existingCustomer);
            logger.info("Customer updated to PENDING-CLOSURE, customer details: {}", customerInPendingClosureState);

            //int a = 90 / 0;
            // STEP 2: close account
            //accountClient.closeAccount(customerId);

            // STEP 3: notify
            //notificationClient.sendNotification("Account closed for " + customerId);

            // STEP-4: mark customer as CLOSED and return a response
            Customer customerInClosedState = customerServiceHelper.markCustomerClosed(customerInPendingClosureState);
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

    @Override
    public void deleteCustomerAndAccount(String customerId) {

    }

    private void compensate(String customerId) {

        try {
            // rollback account state
            //accountClient.rollbackAccount(customerId);

            // reopen customer if partially closed
            customerServiceHelper.reopenCustomer(customerId);

        } catch (Exception e) {
            logger.error(
                    "COMPENSATION FAILED for customerID: {}",
                    customerId,
                    e
            );
        }
    }
}

