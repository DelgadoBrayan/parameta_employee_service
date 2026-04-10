package com.parameta.employee_service.domain.usecase;


import com.parameta.employee_service.domain.exceptions.EmployeeValidationException;
import com.parameta.employee_service.domain.models.Employee;
import com.parameta.employee_service.domain.ports.in.RegisterEmployeeUseCase;
import com.parameta.employee_service.domain.ports.out.EmployeeRepositoryPort;

import java.time.LocalDate;
import java.time.Period;

public class RegisterEmployeeUseCaseImpl implements RegisterEmployeeUseCase {

    private final EmployeeRepositoryPort repositoryPort;

    public RegisterEmployeeUseCaseImpl(EmployeeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Employee register(Employee employee) {
        validateAge(employee.getBirthDate());
        validateHiringDateNotFuture(employee.getHiringDate());
        return repositoryPort.save(employee);
    }

    private void validateAge(LocalDate birthDate) {
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age < 18) {
            throw new EmployeeValidationException("Employee must be at least 18 years old.");
        }
    }

    private void validateHiringDateNotFuture(LocalDate hiringDate) {
        if (hiringDate.isAfter(LocalDate.now())) {
            throw new EmployeeValidationException("Hiring date cannot be in the future.");
        }
    }
}