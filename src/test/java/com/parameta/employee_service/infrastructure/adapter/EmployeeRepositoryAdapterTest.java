package com.parameta.employee_service.infrastructure.adapter;

import com.parameta.employee_service.domain.exceptions.DatabasePersistenceException;
import com.parameta.employee_service.domain.models.Employee;
import com.parameta.employee_service.infrastructure.entity.EmployeeEntity;
import com.parameta.employee_service.infrastructure.repository.EmployeeJpaRepository;
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
import static org.mockito.BDDMockito.willThrow;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmployeeRepositoryAdapter")
class EmployeeRepositoryAdapterTest {

    @Mock
    private EmployeeJpaRepository jpaRepository;

    private EmployeeRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new EmployeeRepositoryAdapter(jpaRepository);
    }

    private Employee buildEmployee() {
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

    private EmployeeEntity buildSavedEntity() {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setId(1L);
        entity.setFirstName("John");
        entity.setLastName("Doe");
        entity.setDocumentType("CC");
        entity.setDocumentNumber("123456789");
        entity.setBirthDate(LocalDate.of(1990, 5, 15));
        entity.setHiringDate(LocalDate.of(2020, 1, 10));
        entity.setPosition("Software Engineer");
        entity.setSalary(new BigDecimal("5000.00"));
        return entity;
    }

    @Nested
    @DisplayName("save()")
    class Save {

        @Test
        @DisplayName("should persist employee and return domain object with generated ID")
        void shouldPersistEmployeeAndReturnWithId() {
            given(jpaRepository.save(any(EmployeeEntity.class))).willReturn(buildSavedEntity());

            Employee result = adapter.save(buildEmployee());

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getFirstName()).isEqualTo("John");
            assertThat(result.getDocumentNumber()).isEqualTo("123456789");
            then(jpaRepository).should().save(any(EmployeeEntity.class));
            then(jpaRepository).should().flush();
        }

        @Test
        @DisplayName("should throw DatabasePersistenceException when JPA throws an exception")
        void shouldThrowDatabasePersistenceExceptionOnJpaFailure() {
            willThrow(new RuntimeException("Duplicate entry"))
                    .given(jpaRepository).save(any(EmployeeEntity.class));

            assertThatThrownBy(() -> adapter.save(buildEmployee()))
                    .isInstanceOf(DatabasePersistenceException.class)
                    .hasMessageContaining("123456789");
        }

        @Test
        @DisplayName("should throw DatabasePersistenceException when flush fails")
        void shouldThrowDatabasePersistenceExceptionWhenFlushFails() {
            given(jpaRepository.save(any(EmployeeEntity.class))).willReturn(buildSavedEntity());
            willThrow(new RuntimeException("Constraint violation"))
                    .given(jpaRepository).flush();

            assertThatThrownBy(() -> adapter.save(buildEmployee()))
                    .isInstanceOf(DatabasePersistenceException.class);
        }

        @Test
        @DisplayName("should map all fields correctly when saving")
        void shouldMapAllFieldsCorrectlyWhenSaving() {
            Employee employee = buildEmployee();
            given(jpaRepository.save(any(EmployeeEntity.class))).willReturn(buildSavedEntity());

            Employee result = adapter.save(employee);

            assertThat(result.getFirstName()).isEqualTo(employee.getFirstName());
            assertThat(result.getLastName()).isEqualTo(employee.getLastName());
            assertThat(result.getDocumentType()).isEqualTo(employee.getDocumentType());
            assertThat(result.getSalary()).isEqualTo(employee.getSalary());
        }
    }
}