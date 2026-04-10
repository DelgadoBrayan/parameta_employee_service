package com.parameta.employee_service.infrastructure.config;


import com.parameta.employee_service.application.services.EmployeeService;
import com.parameta.employee_service.application.services.impl.EmployeeServiceImpl;
import com.parameta.employee_service.domain.ports.in.RegisterEmployeeUseCase;
import com.parameta.employee_service.domain.ports.out.EmployeeRepositoryPort;
import com.parameta.employee_service.domain.usecase.RegisterEmployeeUseCaseImpl;
import com.parameta.employee_service.infrastructure.adapter.EmployeeRepositoryAdapter;
import com.parameta.employee_service.infrastructure.repository.EmployeeJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public EmployeeRepositoryPort employeeRepositoryPort(EmployeeJpaRepository jpaRepository) {
        return new EmployeeRepositoryAdapter(jpaRepository);
    }

    @Bean
    public RegisterEmployeeUseCase registerEmployeeUseCase(EmployeeRepositoryPort repositoryPort) {
        return new RegisterEmployeeUseCaseImpl(repositoryPort);
    }

    @Bean
    public EmployeeService employeeService(RegisterEmployeeUseCase useCase) {
        return new EmployeeServiceImpl(useCase);
    }
}