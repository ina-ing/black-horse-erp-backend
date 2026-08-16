package com.inaing.blackhorse_erp.module.employee.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.inaing.blackhorse_erp.common.dto.ApiResponse;
import com.inaing.blackhorse_erp.module.employee.dto.EmployeeResponseDto;
import com.inaing.blackhorse_erp.module.employee.dto.request.EmployeeCreationRequestDto;
import com.inaing.blackhorse_erp.module.employee.dto.request.EmployeeUpdateRequestDto;
import com.inaing.blackhorse_erp.module.employee.usecase.IEmployeeUseCases;
import com.inaing.blackhorse_erp.module.role.domain.Role;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final IEmployeeUseCases employeeUseCases;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<EmployeeResponseDto> create(@Valid @RequestBody EmployeeCreationRequestDto request) {
        return ApiResponse.created("Employee created", employeeUseCases.create(request));
    }

    @PutMapping("/{identifier}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<EmployeeResponseDto> update(@PathVariable String identifier,
            @Valid @RequestBody EmployeeUpdateRequestDto request) {
        return ApiResponse.ok("Employee updated", employeeUseCases.update(identifier, request));
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<EmployeeResponseDto>> getAllEmployees() {
        return ApiResponse.ok(employeeUseCases.getAllEmployees());
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<EmployeeResponseDto>> getEmployeesByRole(@PathVariable Role role) {
        return ApiResponse.ok(employeeUseCases.getEmployeesByRole(role));
    }

    @GetMapping("/{identifier}")
    public ApiResponse<EmployeeResponseDto> getByIdentifier(@PathVariable String identifier) {
        return ApiResponse.ok(employeeUseCases.getByIdentifier(identifier));
    }

}
