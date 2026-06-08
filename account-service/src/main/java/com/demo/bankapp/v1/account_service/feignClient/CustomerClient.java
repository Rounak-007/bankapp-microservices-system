package com.demo.bankapp.v1.account_service.feignClient;

import com.demo.bankapp.v1.account_service.dto.CustomerResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service",
        configuration = CustomerFeignClientConfiguration.class)
public interface CustomerClient {

    @GetMapping("/api/customers/{customerId}")
    CustomerResponseDto getCustomerDetailsFromCustomerService(@PathVariable String customerId);
}
