package com.demo.bankapp.v1.account_service.feignClient;

import com.demo.bankapp.v1.account_service.exceptions.classes.CustomerNotFoundException;
import com.demo.bankapp.v1.account_service.exceptions.classes.DownstreamServiceUnavailableException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerServiceErrorDecoder implements ErrorDecoder {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceErrorDecoder.class);

    @Override
    public Exception decode(String methodKey, Response response) {

        logger.error("Feign call to Customer Service FAILED on method {} with status {}", methodKey, response.status());

        // 503 Service Unavailable or 504 Gateway Timeout
        if (response.status() == 503 || response.status() == 504) {
            return new DownstreamServiceUnavailableException(
                    "The Customer Service is temporarily unavailable. Please try again later."
            );
        }

        if (response.status() == 404) {
            return new CustomerNotFoundException("Customer not found");
        }

        return new Default().decode(methodKey, response);
    }
}