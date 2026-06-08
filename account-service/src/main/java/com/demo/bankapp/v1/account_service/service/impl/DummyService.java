//package com.demo.bankapp.v1.account_service.service.impl;
//
//import feign.FeignException;
//
//public class DummyService {
//
//    private final AccountRepository accountRepository;
//    private final CustomerClient customerClient;
//
//    public AccountServiceImpl(AccountRepository accountRepository, CustomerClient customerClient) {
//        this.accountRepository = accountRepository;
//        this.customerClient = customerClient;
//    }
//
//    public ResponseEntity<String> createAccount(Account account) {
//        // TODO Auto-generated method stub
//        try {
//            customerClient.getCustomerDetailsFromCustomerService(account.getCustomerId());
//            Account existingAccount = accountRepository.findByCustomerId(account.getCustomerId());
//            if (existingAccount != null) {
//                String msg = "Account already exists for customer ID: " + account.getCustomerId() + " AccountId is..."
//                        + existingAccount.getAccountId();
//                return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
//            } else {
//                Account savedAccount = accountRepository.save(account);
//                String msg = "Account Created Successfully With Account ID : " + savedAccount.getAccountId();
//                return ResponseEntity.status(HttpStatus.OK).body(msg);
//            }
//        } catch (FeignException.NotFound ex) {
//            // Customer Service returned 404
//            throw new RemoteServiceException(ErrorParser(ex.getMessage()));
//        } catch (FeignException.BadRequest ex) {
//            // Customer Service returned 400
//            System.out.println("err..." + ex.getMessage());
//            throw new RemoteServiceException(ErrorParser(ex.getMessage()));
//        } catch (FeignException.InternalServerError ex) {
//            // Customer Service returned 500
//            throw new RemoteServiceException(ErrorParser(ex.getMessage()));
//        } catch (Exception ex) {
//            String errorMessage = "Internal Server Error Occurs...";
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(errorMessage + ErrorParser(ex.getMessage()));
//        }
//    }
//
//    @Override
//    public List<Account> getAllAccounts() {
//        // TODO Auto-generated method stub
//        List<Account> accountList = accountRepository.findAll();
//        // accountList.stream().map(account -> {
//        // account.setCustomerDetails(customerClient.getCustomerDetailsFromCustomerService(account.getCustomerId()));
//        // return account;
//        // }).collect(Collectors.toList());
//        return accountList;
//    }
//
//    @Override
//    public Account getAccount(Long accountId) {
//        // TODO Auto-generated method stub
//        return accountRepository.findById(accountId).orElseThrow(
//                () -> new AccountNotExistException("Account does not exist for Account Id..." + accountId));
//    }
//
//    @Override
//    public Account getAccountByCustomerId(Long customerId) {
//        // TODO Auto-generated method stub
//        try {
//            customerClient.getCustomerDetailsFromCustomerService(customerId);
//            Account account = accountRepository.findByCustomerId(customerId);
//            if (account == null) {
//                throw new AccountNotExistException("Account not exist for customer ID.." + customerId);
//            }
//            return account;
//        } catch (FeignException.NotFound ex) {
//            // Customer Service returned 404
//            throw new RemoteServiceException(ErrorParser(ex.getMessage()));
//        } catch (FeignException.BadRequest ex) {
//            // Customer Service returned 400
//            throw new RemoteServiceException(ErrorParser(ex.getMessage()));
//        } catch (FeignException.InternalServerError ex) {
//            // Customer Service returned 500
//            throw new RemoteServiceException(ErrorParser(ex.getMessage()));
//        } catch (FeignException ex) {
//            // Any other Feign-related issue (timeouts, etc.)
//            throw new RemoteServiceException(
//                    "Unexpected error while communicating with Customer Service: " + ex.status());
//        }
//    }
//
//    @Override
//    public AccountDetails getAccountDetailsByCustomerId(Long customerId) {
//        // TODO Auto-generated method stub
//        AccountDetails accountDetails = new AccountDetails();
//        try {
//            accountDetails.setCustomerDetails(customerClient.getCustomerDetailsFromCustomerService(customerId));
//            Account account = accountRepository.findByCustomerId(customerId);
//            if (account == null) {
//                throw new AccountNotExistException("Account not exist for customer ID.." + customerId);
//            }
//            accountDetails.setAccount(account);
//            return accountDetails;
//        } catch (FeignException.NotFound ex) {
//            // Customer Service returned 404
//            throw new RemoteServiceException(ErrorParser(ex.getMessage()));
//        } catch (FeignException.BadRequest ex) {
//            // Customer Service returned 400
//            throw new RemoteServiceException(ErrorParser(ex.getMessage()));
//        } catch (FeignException.InternalServerError ex) {
//            // Customer Service returned 500
//            throw new RemoteServiceException(ErrorParser(ex.getMessage()));
//        } catch (FeignException ex) {
//            // Any other Feign-related issue (timeouts, etc.)
//            throw new RemoteServiceException(
//                    "Unexpected error while communicating with Customer Service: " + ex.status());
//        }
//    }
//
//    @Override
//    public ResponseEntity<?> getAccountDetails(Long accountId) {
//        // TODO Auto-generated method stub
//        AccountDetails accountDetails = new AccountDetails();
//        try {
//            Account account = getAccount(accountId);
//            accountDetails
//                    .setCustomerDetails(customerClient.getCustomerDetailsFromCustomerService(account.getCustomerId()));
//            accountDetails.setAccount(account);
//            return ResponseEntity.ok(accountDetails);
//        } catch (FeignException.NotFound ex) {
//            // Customer Service returned 404
//            throw new RemoteServiceException(ErrorParser(ex.getMessage()));
//        } catch (FeignException.BadRequest ex) {
//            // Customer Service returned 400
//            throw new RemoteServiceException(ErrorParser(ex.getMessage()));
//        } catch (FeignException.InternalServerError ex) {
//            // Customer Service returned 500
//            throw new RemoteServiceException(ErrorParser(ex.getMessage()));
//        } catch (FeignException ex) {
//            // Any other Feign-related issue (timeouts, etc.)
//            throw new RemoteServiceException(
//                    "Unexpected error while communicating with Customer Service: " + ex.status());
//        } catch (RuntimeException ex) {
//            String errorMessage = "Some Unexpected error occurs..." + ex.getMessage();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
//        }
//    }
//
//    @Override
//    public ResponseEntity<Account> updateAccount(Long accountId, Account updatedAccount) {
//        // TODO Auto-generated method stub
//        Account existingAccount = getAccount(accountId);
//        existingAccount.setBalance(updatedAccount.getBalance());
//        return new ResponseEntity<>(accountRepository.save(existingAccount), HttpStatus.OK);
//    }
//
//    @Override
//    public ResponseEntity<?> depositMoney(Long customerId, float balance) {
//        // TODO Auto-generated method stub
//        try {
//            customerClient.getCustomerDetailsFromCustomerService(customerId);
//            Account account = getAccountByCustomerId(customerId);
//            System.out.println("Current Balance: " + account.getBalance());
//            System.out.println("Deposit Amount: " + balance);
//            float newBalance = account.getBalance() + balance;
//            account.setBalance(newBalance);
//            accountRepository.save(account);
//            System.out.println("Updated Balance: " + account.getBalance());
//            return ResponseEntity.ok(account);
//        } catch (FeignException.NotFound ex) {
//            // Customer service returned 404
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorParser(ex.getMessage()));
//        } catch (FeignException.BadRequest ex) {
//            // TODO: handle exception if Customer service returned 400
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorParser(ex.getMessage()));
//        } catch (FeignException.InternalServerError ex) {
//            // TODO: handle exception if Customer service returned 500
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorParser(ex.getMessage()));
//        } catch (RuntimeException ex) {
//            String errorMessage = "Some Unexpected error occurs: Money can't be deposited..." + ex.getMessage();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
//        }
//    }
//
//    @Override
//    public ResponseEntity<?> withdrawMoney(Long customerId, float amtToBeWithdrawn) {
//        // TODO Auto-generated method stub
//        try {
//            customerClient.getCustomerDetailsFromCustomerService(customerId);
//            Account account = getAccountByCustomerId(customerId);
//            float availableBalance = account.getBalance();
//            if (amtToBeWithdrawn < availableBalance) {
//                System.out.println("Current Balance: " + availableBalance);
//                System.out.println("Withdrawal Amount: " + amtToBeWithdrawn);
//                float newBalance = availableBalance - amtToBeWithdrawn;
//                account.setBalance(newBalance);
//                accountRepository.save(account);
//                System.out.println("Updated Balance: " + account.getBalance());
//                return ResponseEntity.ok(account);
//            } else {
//                String errorMessage = "Insufficient Balance For Withdrawal : Available Balance: " + availableBalance;
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
//            }
//        } catch (FeignException.NotFound ex) {
//            // Customer service returned 404
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorParser(ex.getMessage()));
//        } catch (FeignException.BadRequest ex) {
//            // TODO: handle exception if Customer service returned 400
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorParser(ex.getMessage()));
//        } catch (FeignException.InternalServerError ex) {
//            // TODO: handle exception if Customer service returned 500
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorParser(ex.getMessage()));
//        } catch (RuntimeException ex) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorParser(ex.getMessage()));
//        }
//    }
//
//    @Override
//    public ResponseEntity<?> updateBalance(Long customerId, float amount, boolean isDeposit) {
//        try {
//            System.out.println("isnide udpatewBalacnes.....");
//            // check if customer exists or not
//            customerClient.getCustomerDetailsFromCustomerService(customerId);
//
//            // Get account & balance details
//            Account account = getAccountByCustomerId(customerId);
//            float currentBalance = account.getBalance();
//            float newBalance;
//
//            // Apply transaction logic
//            if (isDeposit) {
//                newBalance = currentBalance + amount;
//            } else {
//                if (amount > currentBalance) {
//                    String msg = "Insufficient Balance For Withdrawal: Available Balance = " + currentBalance;
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
//                }
//                newBalance = currentBalance - amount;
//            }
//
//            // Save updated balance
//            account.setBalance(newBalance);
//            accountRepository.save(account);
//
//            System.out.println("Updated Balance: " + newBalance);
//            return ResponseEntity.ok(account);
//
//        } catch (FeignException.NotFound ex) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorParser(ex.getMessage()));
//        } catch (FeignException.BadRequest ex) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorParser(ex.getMessage()));
//        } catch (FeignException.InternalServerError ex) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorParser(ex.getMessage()));
//        } catch (RuntimeException ex) {
//            String msg = "Unexpected error: " + ex.getMessage();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
//        }
//    }
//
//    @Override
//    public ResponseEntity<String> deleteAccount(Long accountId) {
//        // TODO Auto-generated method stub
//        System.out.println("Received DELETE request for account ID in service layer: " + accountId);
//        getAccount(accountId);
//        accountRepository.deleteById(accountId);
//        return ResponseEntity.status(HttpStatus.OK).body("Account has been successfully deleted...");
//    }
//
//    @Override
//    public ResponseEntity<String> deleteAccountByCustomerId(Long customerId) {
//        // TODO Auto-generated method stub
//        try {
//            Account account = getAccountByCustomerId(customerId);
//            accountRepository.delete(account);
//            return ResponseEntity.status(HttpStatus.OK).body("Account has been successfully deleted...");
//        } catch (Exception e) {
//            // TODO: handle exception
//            return new ResponseEntity<String>("Failed to delete account..." + e.getMessage(),
//                    HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }
//
//    @Override
//    public Boolean checkInvalidId(Long id) {
//        return id < 1;
//    }
//
//    @Override
//    public void freezeAccount(Long customerId) {
//
//    }
//
//    @Override
//    public void closeAccount(Long customerId) {
//
//    }
//
//    @Override
//    public void rollbackAccount(Long customerId) {
//
//    }
//
//    public String ErrorParser(String errorMessage) {
//        String description = "";
//        int start = errorMessage.lastIndexOf('[');
//        int end = errorMessage.lastIndexOf(']');
//
//        if (start != -1 && end != -1 && end > start) {
//            description = errorMessage.substring(start + 1, end);
//            System.out.println(description);
//        }
//        return description;
//    }
//}
