package com.parameta.employee_service.domain.usecase;


import com.parameta.employee_service.domain.exceptions.EmployeeValidationException;
import com.parameta.employee_service.domain.models.Employee;
import com.parameta.employee_service.domain.ports.out.EmployeeRepositoryPort;
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
@DisplayName("RegisterEmployeeUseCase")
class RegisterEmployeeUseCaseImplTest {

    @Mock
    private EmployeeRepositoryPort repositoryPort;

    private RegisterEmployeeUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new RegisterEmployeeUseCaseImpl(repositoryPort);
    }

    private Employee buildValidEmployee() {
        Employee employee = new Employee();
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
    @DisplayName("register()")
    class Register {

        @Test
        @DisplayName("should register employee successfully when all data is valid")
        void shouldRegisterEmployeeSuccessfully() {
            Employee employee = buildValidEmployee();
            Employee savedEmployee = buildValidEmployee();
            savedEmployee.setId(1L);

            given(repositoryPort.save(any(Employee.class))).willReturn(savedEmployee);

            Employee result = useCase.register(employee);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            then(repositoryPort).should().save(employee);
        }

        @Test
        @DisplayName("should throw EmployeeValidationException when employee is under 18")
        void shouldThrowExceptionWhenEmployeeIsUnder18() {
            Employee employee = buildValidEmployee();
            employee.setBirthDate(LocalDate.now().minusYears(17));

            assertThatThrownBy(() -> useCase.register(employee))
                    .isInstanceOf(EmployeeValidationException.class)
                    .hasMessageContaining("18");

            then(repositoryPort).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("should throw EmployeeValidationException when employee is exactly 17 years old")
        void shouldThrowExceptionWhenEmployeeIsExactly17() {
            Employee employee = buildValidEmployee();
            employee.setBirthDate(LocalDate.now().minusYears(17).plusDays(1));

            assertThatThrownBy(() -> useCase.register(employee))
                    .isInstanceOf(EmployeeValidationException.class);

            then(repositoryPort).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("should register employee when employee is exactly 18 years old")
        void shouldRegisterWhenEmployeeIsExactly18() {
            Employee employee = buildValidEmployee();
            employee.setBirthDate(LocalDate.now().minusYears(18));

            Employee savedEmployee = buildValidEmployee();
            savedEmployee.setId(1L);
            given(repositoryPort.save(any(Employee.class))).willReturn(savedEmployee);

            Employee result = useCase.register(employee);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("should throw EmployeeValidationException when hiring date is in the future")
        void shouldThrowExceptionWhenHiringDateIsInFuture() {
            Employee employee = buildValidEmployee();
            employee.setHiringDate(LocalDate.now().plusDays(1));

            assertThatThrownBy(() -> useCase.register(employee))
                    .isInstanceOf(EmployeeValidationException.class)
                    .hasMessageContaining("future");

            then(repositoryPort).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("should register employee when hiring date is today")
        void shouldRegisterWhenHiringDateIsToday() {
            Employee employee = buildValidEmployee();
            employee.setHiringDate(LocalDate.now());

            Employee savedEmployee = buildValidEmployee();
            savedEmployee.setId(1L);
            given(repositoryPort.save(any(Employee.class))).willReturn(savedEmployee);

            Employee result = useCase.register(employee);

            assertThat(result).isNotNull();
        }
    }
}