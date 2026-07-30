package com.inaing.blackhorse_erp.module.employee.usecase;

import com.inaing.blackhorse_erp.module.employee.dto.EmployeeCreationRequestDto;
import com.inaing.blackhorse_erp.module.employee.dto.EmployeeResponseDto;

public interface IEmployeeUseCases {
    EmployeeResponseDto create(EmployeeCreationRequestDto request);
}
