package com.parameta.employee_service.application.services.impl;


import com.parameta.employee_service.application.dtos.EmployeeRequestDto;
import com.parameta.employee_service.application.dtos.EmployeeResponseDto;
import com.parameta.employee_service.domain.exceptions.EmployeeValidationException;
import com.parameta.employee_service.domain.models.Employee;
import com.parameta.employee_service.domain.ports.in.RegisterEmployeeUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmployeeServiceImpl")
class EmployeeServiceImplTest {

    @Mock
    private RegisterEmployeeUseCase registerEmployeeUseCase;

    private EmployeeServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new EmployeeServiceImpl(registerEmployeeUseCase);
    }

    private EmployeeRequestDto buildValidRequest() {
        EmployeeRequestDto dto = new EmployeeRequestDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setDocumentType("CC");
        dto.setDocumentNumber("123456789");
        dto.setBirthDate("1990-05-15");
        dto.setHiringDate("2020-01-10");
        dto.setPosition("Software Engineer");
        dto.setSalary(new BigDecimal("5000.00"));
        return dto;
    }

    private Employee buildSavedEmployee() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setDocumentType("CC");
        employee.setDocumentNumber("123456789");
        employee.setBirthDate(LocalDate.of(1990, 5, 15));
        employee.setHiringDate(LocalDate.of(2020, 1, 10));
        employee.setPosition("Software Engineer");
        employee.setSalary(new BigDecimal("5000.00"));
        return employee;
    }

    @Nested
    @DisplayName("registerEmployee()")
    class RegisterEmployee {

        @Test
        @DisplayName("should return response DTO with computed fields when registration succeeds")
        void shouldReturnResponseWithComputedFields() {
            given(registerEmployeeUseCase.register(any(Employee.class)))
                    .willReturn(buildSavedEmployee());

            EmployeeResponseDto result = service.registerEmployee(buildValidRequest());

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getFirstName()).isEqualTo("John");
            assertThat(result.getCurrentAgeYears()).isNotNull();
            assertThat(result.getSeniorityYears()).isNotNull();
            then(registerEmployeeUseCase).should().register(any(Employee.class));
        }

        @Test
        @DisplayName("should propagate EmployeeValidationException from use case")
        void shouldPropagateValidationException() {
            given(registerEmployeeUseCase.register(any(Employee.class)))
                    .willThrow(new EmployeeValidationException("Employee must be at least 18 years old."));

            assertThatThrownBy(() -> service.registerEmployee(buildValidRequest()))
                    .isInstanceOf(EmployeeValidationException.class)
                    .hasMessageContaining("18");
        }

        @Test
        @DisplayName("should throw EmployeeValidationException when birthDate has invalid format")
        void shouldThrowExceptionWhenBirthDateHasInvalidFormat() {
            EmployeeRequestDto dto = buildValidRequest();
            dto.setBirthDate("not-a-date");

            assertThatThrownBy(() -> service.registerEmployee(dto))
                    .isInstanceOf(EmployeeValidationException.class)
                    .hasMessageContaining("birthDate");

            then(registerEmployeeUseCase).shouldHaveNoInteractions();
        }
    }
}