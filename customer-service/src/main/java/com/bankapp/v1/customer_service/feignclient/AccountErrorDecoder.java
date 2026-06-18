package com.bankapp.v1.customer_service.feignclient;

import com.bankapp.v1.customer_service.exceptions.classes.DownstreamServiceUnavailableException;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class AccountErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 503 || response.status() == 504) {

            //Turning the response into a proper FeignException object
            FeignException feignException = FeignException.errorStatus(methodKey, response);

            return new DownstreamServiceUnavailableException(
                    "The Account Service is temporarily unavailable. Please try again later.",feignException);
        }
        return new Default().decode(methodKey, response);
    }
}
