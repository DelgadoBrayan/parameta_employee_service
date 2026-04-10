package com.parameta.employee_service.application.mappers;


import com.parameta.employee_service.application.dtos.EmployeeRequestDto;
import com.parameta.employee_service.application.dtos.EmployeeResponseDto;
import com.parameta.employee_service.domain.exceptions.EmployeeValidationException;
import com.parameta.employee_service.domain.models.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.BIG_DECIMAL;

@DisplayName("EmployeeMapper")
class EmployeeMapperTest {

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

    private Employee buildValidEmployee() {
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
    @DisplayName("toDomain()")
    class ToDomain {

        @Test
        @DisplayName("should map all fields correctly from request DTO to domain")
        void shouldMapAllFieldsCorrectly() {
            EmployeeRequestDto dto = buildValidRequest();

            Employee result = EmployeeMapper.toDomain(dto);

            assertThat(result.getFirstName()).isEqualTo("John");
            assertThat(result.getLastName()).isEqualTo("Doe");
            assertThat(result.getDocumentType()).isEqualTo("CC");
            assertThat(result.getDocumentNumber()).isEqualTo("123456789");
            assertThat(result.getBirthDate()).isEqualTo(LocalDate.of(1990, 5, 15));
            assertThat(result.getHiringDate()).isEqualTo(LocalDate.of(2020, 1, 10));
            assertThat(result.getPosition()).isEqualTo("Software Engineer");
            assertThat(result.getSalary()).isEqualTo(new BigDecimal("5000.00"));
        }

        @Test
        @DisplayName("should throw EmployeeValidationException when birthDate format is invalid")
        void shouldThrowExceptionWhenBirthDateFormatIsInvalid() {
            EmployeeRequestDto dto = buildValidRequest();
            dto.setBirthDate("15-05-1990");

            assertThatThrownBy(() -> EmployeeMapper.toDomain(dto))
                    .isInstanceOf(EmployeeValidationException.class)
                    .hasMessageContaining("birthDate")
                    .hasMessageContaining("yyyy-MM-dd");
        }

        @Test
        @DisplayName("should throw EmployeeValidationException when hiringDate format is invalid")
        void shouldThrowExceptionWhenHiringDateFormatIsInvalid() {
            EmployeeRequestDto dto = buildValidRequest();
            dto.setHiringDate("2020/01/10");

            assertThatThrownBy(() -> EmployeeMapper.toDomain(dto))
                    .isInstanceOf(EmployeeValidationException.class)
                    .hasMessageContaining("hiringDate");
        }

        @Test
        @DisplayName("should throw EmployeeValidationException when date value is not a real date")
        void shouldThrowExceptionWhenDateIsNotReal() {
            EmployeeRequestDto dto = buildValidRequest();
            dto.setBirthDate("1990-13-45");

            assertThatThrownBy(() -> EmployeeMapper.toDomain(dto))
                    .isInstanceOf(EmployeeValidationException.class);
        }
    }

    @Nested
    @DisplayName("toResponse()")
    class ToResponse {

        @Test
        @DisplayName("should map all base fields correctly from domain to response DTO")
        void shouldMapAllBaseFieldsCorrectly() {
            Employee employee = buildValidEmployee();

            EmployeeResponseDto result = EmployeeMapper.toResponse(employee);

            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getFirstName()).isEqualTo("John");
            assertThat(result.getLastName()).isEqualTo("Doe");
            assertThat(result.getDocumentType()).isEqualTo("CC");
            assertThat(result.getDocumentNumber()).isEqualTo("123456789");
            assertThat(result.getBirthDate()).isEqualTo("1990-05-15");
            assertThat(result.getHiringDate()).isEqualTo("2020-01-10");
            assertThat(result.getPosition()).isEqualTo("Software Engineer");
            assertThat(result.getSalary()).isEqualTo(new BigDecimal("5000.00"));
        }

        @Test
        @DisplayName("should calculate current age correctly")
        void shouldCalculateCurrentAgeCorrectly() {
            Employee employee = buildValidEmployee();
            Period expectedAge = Period.between(employee.getBirthDate(), LocalDate.now());

            EmployeeResponseDto result = EmployeeMapper.toResponse(employee);

            assertThat(result.getCurrentAgeYears()).isEqualTo(String.valueOf(expectedAge.getYears()));
            assertThat(result.getCurrentAgeMonths()).isEqualTo(String.valueOf(expectedAge.getMonths()));
            assertThat(result.getCurrentAgeDays()).isEqualTo(String.valueOf(expectedAge.getDays()));
        }

        @Test
        @DisplayName("should calculate seniority correctly")
        void shouldCalculateSeniorityCorrectly() {
            Employee employee = buildValidEmployee();
            Period expectedSeniority = Period.between(employee.getHiringDate(), LocalDate.now());

            EmployeeResponseDto result = EmployeeMapper.toResponse(employee);

            assertThat(result.getSeniorityYears()).isEqualTo(String.valueOf(expectedSeniority.getYears()));
            assertThat(result.getSeniorityMonths()).isEqualTo(String.valueOf(expectedSeniority.getMonths()));
        }

        @Test
        @DisplayName("should return zero seniority when hiring date is today")
        void shouldReturnZeroSeniorityWhenHiringDateIsToday() {
            Employee employee = buildValidEmployee();
            employee.setHiringDate(LocalDate.now());

            EmployeeResponseDto result = EmployeeMapper.toResponse(employee);

            assertThat(result.getSeniorityYears()).isEqualTo("0");
            assertThat(result.getSeniorityMonths()).isEqualTo("0");
        }
    }
}