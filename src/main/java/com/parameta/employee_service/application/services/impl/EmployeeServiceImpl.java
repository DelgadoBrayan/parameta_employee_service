package com.parameta.employee_service.application.services.impl;


import com.parameta.employee_service.application.dtos.EmployeeRequestDto;
import com.parameta.employee_service.application.dtos.EmployeeResponseDto;
import com.parameta.employee_service.application.mappers.EmployeeMapper;
import com.parameta.employee_service.application.services.EmployeeService;
import com.parameta.employee_service.domain.models.Employee;
import com.parameta.employee_service.domain.ports.in.RegisterEmployeeUseCase;

public class EmployeeServiceImpl implements EmployeeService {

    private final RegisterEmployeeUseCase registerEmployeeUseCase;

    public EmployeeServiceImpl(RegisterEmployeeUseCase registerEmployeeUseCase) {
        this.registerEmployeeUseCase = registerEmployeeUseCase;
    }

    @Override
    public EmployeeResponseDto registerEmployee(EmployeeRequestDto requestDto) {
        Employee employee = EmployeeMapper.toDomain(requestDto);
        Employee saved = registerEmployeeUseCase.register(employee);
        return EmployeeMapper.toResponse(saved);
    }
}