package com.parameta.employee_service.domain.ports.in;


import com.parameta.employee_service.domain.models.Employee;

public interface RegisterEmployeeUseCase {

    Employee register(Employee employee);
}