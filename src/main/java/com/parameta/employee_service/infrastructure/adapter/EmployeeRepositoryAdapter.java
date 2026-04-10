package com.parameta.employee_service.infrastructure.adapter;


import com.parameta.employee_service.domain.exceptions.DatabasePersistenceException;
import com.parameta.employee_service.domain.models.Employee;
import com.parameta.employee_service.domain.ports.out.EmployeeRepositoryPort;
import com.parameta.employee_service.infrastructure.entity.EmployeeEntity;
import com.parameta.employee_service.infrastructure.repository.EmployeeJpaRepository;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class EmployeeRepositoryAdapter implements EmployeeRepositoryPort {

    private final EmployeeJpaRepository jpaRepository;

    public EmployeeRepositoryAdapter(EmployeeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional(
            propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            rollbackFor = Exception.class
    )
    public Employee save(Employee employee) {
        try {
            EmployeeEntity entity = toEntity(employee);
            EmployeeEntity saved = jpaRepository.save(entity);
            jpaRepository.flush();
            return toDomain(saved);
        } catch (Exception e) {
            throw new DatabasePersistenceException(
                    "Failed to persist employee with document: " + employee.getDocumentNumber(), e
            );
        }
    }

    private EmployeeEntity toEntity(Employee employee) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setFirstName(employee.getFirstName());
        entity.setLastName(employee.getLastName());
        entity.setDocumentType(employee.getDocumentType());
        entity.setDocumentNumber(employee.getDocumentNumber());
        entity.setBirthDate(employee.getBirthDate());
        entity.setHiringDate(employee.getHiringDate());
        entity.setPosition(employee.getPosition());
        entity.setSalary(employee.getSalary());
        return entity;
    }

    private Employee toDomain(EmployeeEntity entity) {
        return new Employee(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getDocumentType(),
                entity.getDocumentNumber(),
                entity.getBirthDate(),
                entity.getHiringDate(),
                entity.getPosition(),
                entity.getSalary()
        );
    }
}