package com.parameta.employee_service.domain.exceptions;

public class EmployeeValidationException extends RuntimeException {

    public EmployeeValidationException(String message) {
        super(message);
    }
}