package com.parameta.employee_service.infrastructure.controller;

import com.parameta.employee_service.application.dtos.EmployeeRequestDto;
import com.parameta.employee_service.application.dtos.EmployeeResponseDto;
import com.parameta.employee_service.application.services.EmployeeService;
import com.parameta.employee_service.infrastructure.exceptionHandler.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
@Tag(name = "Employees", description = "Operations for employee registration and management")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(
            summary = "Register a new employee",
            description = "Validates employee data (non-empty fields, date formats, minimum age of 18), " +
                    "persists the record, and returns the saved employee enriched with seniority and current age."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Employee registered successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = EmployeeResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request — field validation failed or business rule violated",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "415",
                    description = "Unsupported media type — Content-Type must be application/json",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error — unexpected failure during processing",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<EmployeeResponseDto> registerEmployee(
            @Valid @RequestBody EmployeeRequestDto requestDto) {
        EmployeeResponseDto response = employeeService.registerEmployee(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}