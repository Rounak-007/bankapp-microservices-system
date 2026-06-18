package com.demo.bankapp.v1.account_service.controller;

import java.util.List;

import com.demo.bankapp.v1.account_service.dto.AccountRequestDto;
import com.demo.bankapp.v1.account_service.dto.AccountResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.demo.bankapp.v1.account_service.configProperties.ConfigProperties;
import com.demo.bankapp.v1.account_service.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
@RefreshScope
@Validated
public class AccountController {

    private final AccountService accountService;
    private final ConfigProperties configProperties;

    //@Value("${message}")
    @Value("${message:Default fallback message}")
    String msg;

    public AccountController(AccountService accountService, ConfigProperties configProperties) {
        this.accountService = accountService;
        this.configProperties = configProperties;
    }

    @GetMapping("/welcome")
    public String welcomeMsg() {
        return "Welcome to Github actions Demo of Account-Service " + msg;
    }

    @GetMapping("/home")
    public String homePageMsg() {
        return "Hello Dear Account-Holder...." + configProperties.getMessage();
    }

    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(@RequestBody @Valid AccountRequestDto requestDto) {
        AccountResponseDto responseDto = accountService.createAccount(requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }

    @GetMapping("/account-number/{accountNumber}")
    public ResponseEntity<AccountResponseDto> getAccountByAccountNumber(
            @PathVariable @Pattern(regexp = "^ACCT-[A-Za-z0-9]{8}$", message = "invalid account number format") String accountNumber) {

        AccountResponseDto response = accountService.getAccountByAccountNumber(accountNumber);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponseDto> getAccountByAccountId(
            @PathVariable Long accountId) {

        AccountResponseDto response = accountService.getAccountByAccountId(accountId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountResponseDto>> getAccountsByCustomerId(
            @PathVariable @Pattern(regexp = "^CUST-[A-Za-z0-9]{8}$", message = "invalid customerId format") String customerId) {
        List<AccountResponseDto> response = accountService.getAccountsByCustomerId(customerId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{accountNumber}/block")
    public ResponseEntity<AccountResponseDto> blockAccount(@PathVariable @Pattern(regexp = "^ACCT-[A-Za-z0-9]{8}$", message = "invalid account number format") String accountNumber) {
        AccountResponseDto response = accountService.blockAccount(accountNumber);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{accountNumber}/activate")
    public ResponseEntity<AccountResponseDto> activateAccount(
            @PathVariable @Pattern(regexp = "^ACCT-[A-Za-z0-9]{8}$", message = "invalid account number format") String accountNumber) {

        AccountResponseDto response = accountService.activateAccount(accountNumber);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{accountNumber}/close")
    public ResponseEntity<AccountResponseDto> closeAccount(
            @PathVariable @Pattern(regexp = "^ACCT-[A-Za-z0-9]{8}$", message = "invalid account number format") String accountNumber) {

        AccountResponseDto response = accountService.closeAccount(accountNumber);
        return ResponseEntity.ok(response);
    }

//
//	@PutMapping("/deposit")
//	public ResponseEntity<?> depositMoney(@RequestParam Long customerId, @RequestParam Float balance) {
//		if(balance==null || customerId==null) {
//			throw new InvalidAmountException("Deposit amount must be greater than zero.");
//		}
//		if (balance <= 0) {
//			throw new InvalidAmountException("Deposit amount must be greater than zero.");
//		}
//		//return accountService.depositMoney(customerId, balance);
//		return accountService.updateBalance(customerId, balance, true);
//	}
//
//	@PutMapping("/withdraw")
//	public ResponseEntity<?> withdrawMoney(@RequestParam Long customerId, @RequestParam float balance) {
//		if (balance <= 0) {
//			throw new InvalidAmountException("Withdraw amount must be greater than zero.");
//		}
//		//return accountService.withdrawMoney(customerId, balance);
//		return accountService.updateBalance(customerId, balance, false);
//	}
//
//	@PutMapping("update/{accountId}")
//	public ResponseEntity<Account> updateAccount(@PathVariable Long accountId, @RequestBody Account updatedAccount) {
//		// TODO: process PUT request
//		if (accountService.checkInvalidId(accountId)) {
//			throw new InvalidIdException(configProperties.getAccountIdExceptionMsg());
//		}
//		return accountService.updateAccount(accountId, updatedAccount);
//	}
//
//	// get all accounts
//	@GetMapping("/list")
//	public List<Account> getAllAccounts() {
//		return accountService.getAllAccounts();
//	}
//
//	// get account by account id
//	@GetMapping("/{accountId}")
//	public Account getAccountDetails(@PathVariable Long accountId) {
//		if (accountService.checkInvalidId(accountId)) {
//			throw new InvalidIdException(configProperties.getAccountIdExceptionMsg());
//		}
//		return accountService.getAccount(accountId);
//	}
//
//	// get customer and account details by account id
//	@GetMapping("/details/{accountId}")
//	public ResponseEntity<?> getDetails(@PathVariable Long accountId) {
//		if (accountService.checkInvalidId(accountId)) {
//			throw new InvalidIdException(configProperties.getAccountIdExceptionMsg());
//		}
//		return accountService.getAccountDetails(accountId);
//	}
//
//	// delete by account id
//	@DeleteMapping("/delete/{accountId}")
//	public ResponseEntity<String> deleteAccount(@PathVariable long accountId) {
//		if (accountService.checkInvalidId(accountId)) {
//			throw new InvalidIdException(configProperties.getAccountIdExceptionMsg());
//		}
//		return accountService.deleteAccount(accountId);
//	}
//
//	// get only account details by customer id
//	@GetMapping("/customer/{customerId}")
//	public Account getAccountOfCustomer(@PathVariable Long customerId) {
//		if (accountService.checkInvalidId(customerId)) {
//			throw new InvalidIdException(configProperties.getCustomerIdExceptionMsg());
//		}
//		return accountService.getAccountByCustomerId(customerId);
//	}
//
//	// get account and customer details by customer id
//	@GetMapping("/details/customer/{customerId}")
//	public AccountDetails getAccountDetailsOfCustomer(@PathVariable Long customerId) {
//		if (accountService.checkInvalidId(customerId)) {
//			throw new InvalidIdException(configProperties.getCustomerIdExceptionMsg());
//		}
//		return accountService.getAccountDetailsByCustomerId(customerId);
//	}
//
//	// delete account by customer id
//	@DeleteMapping("/delete/customer/{customerId}")
//	public ResponseEntity<String> deleteAccountByCustomerId(@PathVariable long customerId) {
//		if (accountService.checkInvalidId(customerId)) {
//			throw new InvalidIdException(configProperties.getCustomerIdExceptionMsg());
//		}
//		return accountService.deleteAccountByCustomerId(customerId);
//	}

}
