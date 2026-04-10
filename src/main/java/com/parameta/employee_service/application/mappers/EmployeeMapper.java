package com.parameta.employee_service.application.mappers;


import com.parameta.employee_service.application.dtos.EmployeeRequestDto;
import com.parameta.employee_service.application.dtos.EmployeeResponseDto;
import com.parameta.employee_service.domain.exceptions.EmployeeValidationException;
import com.parameta.employee_service.domain.models.Employee;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EmployeeMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private EmployeeMapper() {}

    public static Employee toDomain(EmployeeRequestDto dto) {
        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setDocumentType(dto.getDocumentType());
        employee.setDocumentNumber(dto.getDocumentNumber());
        employee.setBirthDate(parseDate(dto.getBirthDate(), "birthDate"));
        employee.setHiringDate(parseDate(dto.getHiringDate(), "hiringDate"));
        employee.setPosition(dto.getPosition());
        employee.setSalary(dto.getSalary());
        return employee;
    }

    public static EmployeeResponseDto toResponse(Employee employee) {
        EmployeeResponseDto dto = new EmployeeResponseDto();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setDocumentType(employee.getDocumentType());
        dto.setDocumentNumber(employee.getDocumentNumber());
        dto.setBirthDate(employee.getBirthDate().format(FORMATTER));
        dto.setHiringDate(employee.getHiringDate().format(FORMATTER));
        dto.setPosition(employee.getPosition());
        dto.setSalary(employee.getSalary());

        LocalDate today = LocalDate.now();

        Period age = Period.between(employee.getBirthDate(), today);
        dto.setCurrentAgeYears(String.valueOf(age.getYears()));
        dto.setCurrentAgeMonths(String.valueOf(age.getMonths()));
        dto.setCurrentAgeDays(String.valueOf(age.getDays()));

        Period seniority = Period.between(employee.getHiringDate(), today);
        dto.setSeniorityYears(String.valueOf(seniority.getYears()));
        dto.setSeniorityMonths(String.valueOf(seniority.getMonths()));

        return dto;
    }

    private static LocalDate parseDate(String value, String fieldName) {
        try {
            return LocalDate.parse(value, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new EmployeeValidationException(
                "Invalid date format for field '" + fieldName + "'. Expected format: yyyy-MM-dd"
            );
        }
    }
}