package com.demo.bankapp.v1.account_service.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.demo.bankapp.v1.account_service.exceptions.classes.*;
import com.demo.bankapp.v1.account_service.exceptions.responses.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotExistException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotExistException(AccountNotExistException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        ex.getMessage(),
                        null,
                        HttpStatus.NOT_FOUND.value(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientFundsException(InsufficientFundsException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        ex.getMessage(),
                        null,
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFound(CustomerNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        ex.getMessage(),
                        null,
                        HttpStatus.NOT_FOUND.value(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidErr(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Validation failed for this request",
                        errors,
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        ex.getMessage(),
                        null,
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        "Malformed JSON request body or invalid input format.",
                        null,
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now()));

    }

    @ExceptionHandler(IllegalAccountStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalAccountStateException(IllegalAccountStateException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        ex.getMessage(),
                        null,
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAccountAlreadyExistsException(AccountAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        ex.getMessage(),
                        null,
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now()));
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
                .status(HttpStatus.BAD_GATEWAY)
                .body(new ErrorResponse(
                        ex.getMessage(),
                        null,
                        HttpStatus.BAD_GATEWAY.value(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {

        Map<String, String> errors = new HashMap<>();

        errors.put(
                ex.getName(),
                "Invalid value '" + ex.getValue() + "' for parameter '" + ex.getName() + "'"
        );

        ErrorResponse errorResponse =
                new ErrorResponse(
                        "Invalid request parameter",
                        errors,
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now()
                );

        return ResponseEntity
                .badRequest()
                .body(errorResponse);
    }

}
