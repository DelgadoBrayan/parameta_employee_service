package com.parameta.employee_service.infrastructure.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.parameta.employee_service.application.dtos.EmployeeRequestDto;
import com.parameta.employee_service.application.dtos.EmployeeResponseDto;
import com.parameta.employee_service.application.services.EmployeeService;
import com.parameta.employee_service.domain.exceptions.EmployeeValidationException;
import com.parameta.employee_service.infrastructure.exceptionHandler.GlobalExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {EmployeeController.class, GlobalExceptionHandler.class})
@DisplayName("EmployeeController")
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmployeeService employeeService;

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

    private EmployeeResponseDto buildResponseDto() {
        EmployeeResponseDto dto = new EmployeeResponseDto();
        dto.setId(1L);
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setDocumentType("CC");
        dto.setDocumentNumber("123456789");
        dto.setBirthDate("1990-05-15");
        dto.setHiringDate("2020-01-10");
        dto.setPosition("Software Engineer");
        dto.setSalary(new BigDecimal("5000.00"));
        dto.setCurrentAgeYears("35");
        dto.setCurrentAgeMonths("10");
        dto.setCurrentAgeDays("26");
        dto.setSeniorityYears("6");
        dto.setSeniorityMonths("3");
        return dto;
    }

    @Nested
    @DisplayName("POST /api/v1/employees")
    class PostEmployee {

        @Test
        @DisplayName("should return 201 CREATED with response body when request is valid")
        void shouldReturn201WhenRequestIsValid() throws Exception {
            given(employeeService.registerEmployee(any(EmployeeRequestDto.class)))
                    .willReturn(buildResponseDto());

            mockMvc.perform(post("/api/v1/employees")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(buildValidRequest())))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.firstName").value("John"))
                    .andExpect(jsonPath("$.documentNumber").value("123456789"))
                    .andExpect(jsonPath("$.currentAgeYears").value("35"))
                    .andExpect(jsonPath("$.seniorityYears").value("6"));
        }

        @Test
        @DisplayName("should return 400 when firstName is blank")
        void shouldReturn400WhenFirstNameIsBlank() throws Exception {
            EmployeeRequestDto dto = buildValidRequest();
            dto.setFirstName("");

            mockMvc.perform(post("/api/v1/employees")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("Invalid Request"));
        }

        @Test
        @DisplayName("should return 400 when salary is negative")
        void shouldReturn400WhenSalaryIsNegative() throws Exception {
            EmployeeRequestDto dto = buildValidRequest();
            dto.setSalary(new BigDecimal("-100.00"));

            mockMvc.perform(post("/api/v1/employees")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400));
        }

        @Test
        @DisplayName("should return 400 when salary is null")
        void shouldReturn400WhenSalaryIsNull() throws Exception {
            EmployeeRequestDto dto = buildValidRequest();
            dto.setSalary(null);

            mockMvc.perform(post("/api/v1/employees")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return 400 when employee is under 18")
        void shouldReturn400WhenEmployeeIsUnder18() throws Exception {
            given(employeeService.registerEmployee(any(EmployeeRequestDto.class)))
                    .willThrow(new EmployeeValidationException("Employee must be at least 18 years old."));

            mockMvc.perform(post("/api/v1/employees")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(buildValidRequest())))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("Validation Error"))
                    .andExpect(jsonPath("$.message").value("Employee must be at least 18 years old."));
        }

        @Test
        @DisplayName("should return 400 when request body is empty")
        void shouldReturn400WhenRequestBodyIsEmpty() throws Exception {
            mockMvc.perform(post("/api/v1/employees")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return 400 when content type is not JSON")
        void shouldReturn400WhenContentTypeIsNotJson() throws Exception {
            mockMvc.perform(post("/api/v1/employees")
                            .contentType(MediaType.TEXT_PLAIN)
                            .content("plain text"))
                    .andExpect(status().is4xxClientError());
        }
    }
}