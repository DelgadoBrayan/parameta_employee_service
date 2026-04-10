package com.parameta.employee_service.domain.ports.out;


import com.parameta.employee_service.domain.models.Employee;

public interface EmployeeRepositoryPort {

    Employee save(Employee employee);
}