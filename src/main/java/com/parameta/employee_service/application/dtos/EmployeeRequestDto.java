package com.parameta.employee_service.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Schema(description = "Request payload for registering a new employee")
@Getter
@Setter
public class EmployeeRequestDto {

    @Schema(description = "Employee's first name", example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "First name is required")
    private String firstName;

    @Schema(description = "Employee's last name", example = "Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Schema(description = "Document type (e.g. CC, CE, PASSPORT)", example = "CC", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Document type is required")
    private String documentType;

    @Schema(description = "Unique document number", example = "123456789", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Document number is required")
    private String documentNumber;

    @Schema(description = "Date of birth in format yyyy-MM-dd. Employee must be at least 18 years old.", example = "1990-05-15", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Birth date is required")
    private String birthDate;

    @Schema(description = "Company hiring date in format yyyy-MM-dd. Cannot be a future date.", example = "2020-01-10", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Hiring date is required")
    private String hiringDate;

    @Schema(description = "Job position or title", example = "Software Engineer", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Position is required")
    private String position;

    @Schema(description = "Monthly salary in USD. Must be greater than zero.", example = "5000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be greater than zero")
    private BigDecimal salary;

}