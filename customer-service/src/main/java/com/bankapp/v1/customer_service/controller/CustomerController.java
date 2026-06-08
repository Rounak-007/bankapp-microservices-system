package com.bankapp.v1.customer_service.controller;

import java.util.List;

import com.bankapp.v1.customer_service.dto.CustomerRequestDto;
import com.bankapp.v1.customer_service.dto.CustomerResponseDto;
import com.bankapp.v1.customer_service.exceptions.DeleteApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankapp.v1.customer_service.service.CustomerService;

@RestController
@Validated
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/test")
    public String test() {
        return "Customer Home Page...";
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDto> createCustomer(@Valid @RequestBody CustomerRequestDto customerRequestDto) {

        CustomerResponseDto response = customerService.createCustomer(customerRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDto> getCustomer(@PathVariable @NotBlank(message = "ID must not be empty") String customerId) {
        return ResponseEntity.ok(customerService.getCustomer(customerId));
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(@PathVariable @NotBlank(message = "ID must not be empty") String customerId, @Valid @RequestBody CustomerRequestDto customerRequestDto) {
        return ResponseEntity.ok(customerService.updateCustomer(customerId, customerRequestDto));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<DeleteApiResponse> deleteCustomer(@PathVariable @NotBlank(message = "ID must not be empty") String customerId) {

        customerService.deleteCustomer(customerId);

        return ResponseEntity.ok(new DeleteApiResponse("Customer deleted successfully!!"));
    }

    @DeleteMapping("/delete/customer-account/{customerId}")
    public ResponseEntity<DeleteApiResponse> deleteCustomerAndAccount(@PathVariable @NotBlank(message = "ID must not be empty") String customerId) {

        customerService.deleteCustomerAndAccount(customerId);

        return ResponseEntity.ok(new DeleteApiResponse("Customer and Account deleted successfully!!"));

    }

    @PostMapping("/{customerId}/close")
    public ResponseEntity<CustomerResponseDto> closeCustomer(@PathVariable @NotBlank(message = "ID must not be empty") String customerId) {
        return ResponseEntity.ok(customerService.closeCustomerRecord(customerId));
    }

    @PostMapping("/{customerId}/reopen")
    public ResponseEntity<CustomerResponseDto> reopenCustomer(@PathVariable @NotBlank(message = "ID must not be empty") String customerId) {
        return ResponseEntity.ok(customerService.reopenCustomer(customerId));
    }
}
