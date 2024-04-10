package com.product.management.system.productmanagementsystem.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Object> createResponseEntity(
            Object error,
            HttpHeaders headers,
            HttpStatus status) {
        return new ResponseEntity<>(error, headers, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
        var errorMessage = e.getConstraintViolations().stream()
                .map(constraintViolation -> String.format("%s: %s",
                        constraintViolation.getPropertyPath(), constraintViolation.getMessage())
                ).collect(Collectors.joining("\n"));
        var headers = HttpHeaders.EMPTY;
        var status = HttpStatus.BAD_REQUEST;
        return this.createResponseEntity(errorMessage, headers, status);
    }

}
