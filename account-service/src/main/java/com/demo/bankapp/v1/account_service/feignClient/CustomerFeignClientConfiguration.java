package com.demo.bankapp.v1.account_service.feignClient;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerFeignClientConfiguration {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomerServiceErrorDecoder();
    }
}