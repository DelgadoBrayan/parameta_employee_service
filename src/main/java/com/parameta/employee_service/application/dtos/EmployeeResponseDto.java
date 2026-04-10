package com.parameta.employee_service.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Schema(description = "Response payload after employee registration")
@Getter
@Setter
public class EmployeeResponseDto {

    @Schema(description = "Generated employee ID", example = "1")
    private Long id;

    @Schema(description = "Employee's first name", example = "John")
    private String firstName;

    @Schema(description = "Employee's last name", example = "Doe")
    private String lastName;

    @Schema(description = "Document type", example = "CC")
    private String documentType;

    @Schema(description = "Document number", example = "123456789")
    private String documentNumber;

    @Schema(description = "Date of birth", example = "1990-05-15")
    private String birthDate;

    @Schema(description = "Company hiring date", example = "2020-01-10")
    private String hiringDate;

    @Schema(description = "Job position", example = "Software Engineer")
    private String position;

    @Schema(description = "Monthly salary", example = "5000.0")
    private BigDecimal salary;

    @Schema(description = "Years with the company", example = "6")
    private String seniorityYears;

    @Schema(description = "Remaining months of seniority beyond full years", example = "3")
    private String seniorityMonths;

    @Schema(description = "Current age in years", example = "35")
    private String currentAgeYears;

    @Schema(description = "Remaining months of age beyond full years", example = "10")
    private String currentAgeMonths;

    @Schema(description = "Remaining days of age beyond full months", example = "26")
    private String currentAgeDays;

}