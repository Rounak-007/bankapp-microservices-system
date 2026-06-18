package com.bankapp.v1.customer_service.exceptions;

import com.bankapp.v1.customer_service.exceptions.classes.*;
import com.bankapp.v1.customer_service.exceptions.responses.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotExistException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFoundException(CustomerNotExistException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        ex.getMessage(),
                        null,
                        HttpStatus.NOT_FOUND.value(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler({DuplicatePhoneNumberException.class, DuplicateEmailException.class})
    public ResponseEntity<ErrorResponse> handleDuplicatePhoneAndEmailException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        ex.getMessage(),
                        null,
                        HttpStatus.CONFLICT.value(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(InvalidCustomerIdException.class)
    public ResponseEntity<String> handleInvalidCustomerIdException(InvalidCustomerIdException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        System.out.println("Inside ConstraintValidationException...");
        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations()
                .forEach(violation -> {
                    String path = violation
                            .getPropertyPath()
                            .toString();

                    String field = path.substring(path.lastIndexOf('.') + 1);
                    errors.put(
                            field,
                            violation.getMessage());
                });

        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(
                                "Validation failed",
                                errors,
                                HttpStatus.BAD_REQUEST.value(),
                                LocalDateTime.now()
                        )
                );
    }

    @ExceptionHandler(DownstreamServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleDownstreamServiceUnavailable(DownstreamServiceUnavailableException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErrorResponse(
                        ex.getMessage(),
                        null,
                        HttpStatus.SERVICE_UNAVAILABLE.value(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        ex.getMessage(),
                        null,
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()));

        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        "Validation Failed for the request",
                        errors,
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now()
                ));


    }
}
