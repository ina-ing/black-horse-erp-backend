package com.inaing.blackhorse_erp.module.employee.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.inaing.blackhorse_erp.common.dto.ApiResponse;
import com.inaing.blackhorse_erp.module.employee.dto.EmployeeCreationRequestDto;
import com.inaing.blackhorse_erp.module.employee.dto.EmployeeResponseDto;
import com.inaing.blackhorse_erp.module.employee.usecase.IEmployeeUseCases;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final IEmployeeUseCases employeeUseCases;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<EmployeeResponseDto> create(@Valid @RequestBody EmployeeCreationRequestDto request) {
        return ApiResponse.created("Employee created", employeeUseCases.create(request));
    }
}
