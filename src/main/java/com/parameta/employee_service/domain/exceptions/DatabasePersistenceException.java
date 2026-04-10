package com.parameta.employee_service.domain.exceptions;

public class DatabasePersistenceException extends RuntimeException {

    public DatabasePersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}