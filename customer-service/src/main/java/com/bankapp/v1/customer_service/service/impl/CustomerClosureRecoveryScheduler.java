package com.bankapp.v1.customer_service.service.impl;


import com.bankapp.v1.customer_service.model.Customer;
import com.bankapp.v1.customer_service.model.CustomerStatus;
import com.bankapp.v1.customer_service.repository.CustomerRepository;

import com.bankapp.v1.customer_service.service.helper.CustomerServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerClosureRecoveryScheduler {

    private static final Logger logger = LoggerFactory.getLogger(CustomerClosureRecoveryScheduler.class);

    private final CustomerRepository customerRepository;
    private final CustomerServiceHelper customerServiceHelper;

    public CustomerClosureRecoveryScheduler(CustomerRepository customerRepository, CustomerServiceHelper customerServiceHelper) {
        this.customerRepository = customerRepository;
        this.customerServiceHelper = customerServiceHelper;
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 60000)
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
                customerServiceHelper.markCustomerClosed(customer);
                logger.info("Recovered closure for customer = {}", customer.getCustomerId());
            } catch (Exception ex) {
                logger.error(
                        "Recovery failed for customer={}",
                        customer.getCustomerId(),
                        ex);
            }
        }
    }
}
