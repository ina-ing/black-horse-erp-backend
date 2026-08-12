package com.inaing.blackhorse_erp.module.employee.usecase.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.inaing.blackhorse_erp.module.employee.dto.EmployeeResponseDto;
import com.inaing.blackhorse_erp.module.employee.dto.request.EmployeeCreationRequestDto;
import com.inaing.blackhorse_erp.module.employee.dto.request.EmployeeUpdateRequestDto;
import com.inaing.blackhorse_erp.module.employee.usecase.IEmployeeUseCases;
import com.inaing.blackhorse_erp.module.employee.usecase.impl.usecases.CreateEmployeeUseCase;
import com.inaing.blackhorse_erp.module.employee.usecase.impl.usecases.GetEmployeeUsecase;
import com.inaing.blackhorse_erp.module.employee.usecase.impl.usecases.GetSalesEmployeesUseCase;
import com.inaing.blackhorse_erp.module.employee.usecase.impl.usecases.UpdateEmployeeUseCase;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmployeeUseCasesImpl implements IEmployeeUseCases {

    private final CreateEmployeeUseCase createEmployeeUseCase;
    private final GetSalesEmployeesUseCase getSalesEmployeesUseCase;
    private final GetEmployeeUsecase getEmployeeUsecase;
    private final UpdateEmployeeUseCase updateEmployeeUseCase;

    @Override
    public EmployeeResponseDto create(EmployeeCreationRequestDto request) {
        return createEmployeeUseCase.execute(request);
    }

    @Override
    public List<EmployeeResponseDto> getSalesEmployees() {
        return getSalesEmployeesUseCase.execute();
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
