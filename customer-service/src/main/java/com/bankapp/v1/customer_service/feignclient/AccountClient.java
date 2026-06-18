package com.bankapp.v1.customer_service.feignclient;

import com.bankapp.v1.customer_service.dto.AccountResponseDto;
import jakarta.validation.constraints.Pattern;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

//@FeignClient(url = "http://localhost:9092", value = "Account-Client")
//@FeignClient(name =  "account-service")
@FeignClient(
        name = "account-service",
        configuration = FeignConfig.class
        //fallbackFactory = AccountFallbackFactory.class
)
public interface AccountClient {

    @GetMapping("api/accounts/customer/{customerId}")
    List<AccountResponseDto> getAccountsByCustomerId(@PathVariable String customerId);

    //delete by account id
    @DeleteMapping("api/accounts/delete/{accountId}")
    ResponseEntity<String> deleteAccount(@PathVariable String accountId);

    //delete account by customer id
    @DeleteMapping("api/accounts/delete/customer/{customerId}")
    ResponseEntity<String> deleteAccountByCustomerId(@PathVariable String customerId);

    @PostMapping("api/accounts/{customerId}/freeze")
    void freezeAccount(@PathVariable String customerId);

    @PostMapping("api/accounts/{customerId}/close")
    void closeAccount(@PathVariable String customerId);

    @PostMapping("api/accounts/{customerId}/rollback")
    void rollbackAccount(@PathVariable String customerId);

}
