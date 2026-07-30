package com.inaing.blackhorse_erp.module.employee.usecase.impl;

import org.springframework.stereotype.Component;

import com.inaing.blackhorse_erp.module.employee.dto.EmployeeCreationRequestDto;
import com.inaing.blackhorse_erp.module.employee.dto.EmployeeResponseDto;
import com.inaing.blackhorse_erp.module.employee.usecase.IEmployeeUseCases;
import com.inaing.blackhorse_erp.module.employee.usecase.impl.usecases.CreateEmployeeUseCase;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmployeeUseCasesImpl implements IEmployeeUseCases {

    private final CreateEmployeeUseCase createEmployeeUseCase;
    @Override
    public EmployeeResponseDto create(EmployeeCreationRequestDto request) {
       return createEmployeeUseCase.execute(request);
    }

}
