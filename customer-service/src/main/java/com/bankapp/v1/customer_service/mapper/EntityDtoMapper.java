package com.bankapp.v1.customer_service.mapper;

import com.bankapp.v1.customer_service.dto.CustomerRequestDto;
import com.bankapp.v1.customer_service.dto.CustomerResponseDto;
import com.bankapp.v1.customer_service.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class EntityDtoMapper {

    public Customer getEntityFromRequestDto(CustomerRequestDto customerRequestDto) {

        Customer customer = new Customer();
        customer.setName(customerRequestDto.name());
        customer.setPhoneNumber(customerRequestDto.phoneNumber());
        customer.setEmail(customerRequestDto.email());
        return customer;
    }

    public CustomerResponseDto getResponseDtoFromEntity(Customer customer) {
        return new CustomerResponseDto(
                customer.getCustomerId(),
                customer.getName(),
                customer.getPhoneNumber(),
                customer.getEmail(),
                customer.getStatus()
        );
    }
}
