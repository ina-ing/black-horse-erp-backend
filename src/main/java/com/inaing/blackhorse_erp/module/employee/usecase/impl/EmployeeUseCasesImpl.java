package com.inaing.blackhorse_erp.module.employee.usecase.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.inaing.blackhorse_erp.common.dto.list.PagedListWithAnalytics;
import com.inaing.blackhorse_erp.module.employee.dto.response.analytics.EmployeeBasicAnalyticsDto;
import com.inaing.blackhorse_erp.module.employee.dto.EmployeeResponseDto;
import com.inaing.blackhorse_erp.module.employee.dto.request.EmployeeCreationRequestDto;
import com.inaing.blackhorse_erp.module.employee.dto.request.EmployeeQueryParams;
import com.inaing.blackhorse_erp.module.employee.dto.request.EmployeeUpdateRequestDto;
import com.inaing.blackhorse_erp.module.employee.usecase.IEmployeeUseCases;
import com.inaing.blackhorse_erp.module.employee.usecase.impl.usecases.CreateEmployeeUseCase;
import com.inaing.blackhorse_erp.module.employee.usecase.impl.usecases.GetAllEmployeesUseCase;
import com.inaing.blackhorse_erp.module.employee.usecase.impl.usecases.GetEmployeeUsecase;
import com.inaing.blackhorse_erp.module.employee.usecase.impl.usecases.GetEmployeesByRoleUseCase;
import com.inaing.blackhorse_erp.module.employee.usecase.impl.usecases.UpdateEmployeeUseCase;
import com.inaing.blackhorse_erp.module.role.domain.Role;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmployeeUseCasesImpl implements IEmployeeUseCases {

    private final CreateEmployeeUseCase createEmployeeUseCase;
    private final GetAllEmployeesUseCase getAllEmployeesUseCase;
    private final GetEmployeeUsecase getEmployeeUsecase;
    private final GetEmployeesByRoleUseCase getEmployeesByRoleUseCase;
    private final UpdateEmployeeUseCase updateEmployeeUseCase;

    @Override
    public EmployeeResponseDto create(EmployeeCreationRequestDto request) {
        return createEmployeeUseCase.execute(request);
    }

    @Override
    public PagedListWithAnalytics<EmployeeBasicAnalyticsDto, EmployeeResponseDto> getAllEmployees(
            EmployeeQueryParams params) {
        return getAllEmployeesUseCase.execute(params);
    }

    @Override
    public List<EmployeeResponseDto> getEmployeesByRole(Role role) {
        return getEmployeesByRoleUseCase.execute(role);
    }

    @Override
    public EmployeeResponseDto getByIdentifier(String identifier) {
        return getEmployeeUsecase.execute(identifier);
    }

    @Override
    public EmployeeResponseDto update(String identifier, EmployeeUpdateRequestDto request) {
        return updateEmployeeUseCase.execute(identifier, request);
    }
}
