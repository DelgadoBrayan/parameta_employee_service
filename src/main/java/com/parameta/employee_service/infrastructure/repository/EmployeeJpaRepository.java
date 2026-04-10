package com.parameta.employee_service.infrastructure.repository;

import com.parameta.employee_service.infrastructure.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeJpaRepository extends JpaRepository<EmployeeEntity, Long> {
}