package com.bankapp.v1.customer_service.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

//@FeignClient(url = "http://localhost:9092", value = "Account-Client")
@FeignClient(name =  "account-service")
public interface AccountClient {

	//delete by account id
	@DeleteMapping("/account/delete/{accountId}")
	ResponseEntity<String> deleteAccount(@PathVariable String accountId);

	//delete account by customer id
	@DeleteMapping("/account/delete/customer/{customerId}")
	ResponseEntity<String> deleteAccountByCustomerId(@PathVariable String customerId);

	@PostMapping("/accounts/{customerId}/freeze")
	void freezeAccount(@PathVariable String customerId);

	@PostMapping("/accounts/{customerId}/close")
	void closeAccount(@PathVariable String customerId);

	@PostMapping("/accounts/{customerId}/rollback")
	void rollbackAccount(@PathVariable String customerId);

}
