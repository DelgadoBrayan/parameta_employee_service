package com.parameta.employee_service.application.services;


import com.parameta.employee_service.application.dtos.EmployeeRequestDto;
import com.parameta.employee_service.application.dtos.EmployeeResponseDto;

public interface EmployeeService {

    EmployeeResponseDto registerEmployee(EmployeeRequestDto requestDto);
}