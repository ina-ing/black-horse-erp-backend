package com.inaing.blackhorse_erp.module.employee.usecase.impl.usecases;

import org.springframework.stereotype.Component;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.employee.dto.EmployeeResponseDto;
import com.inaing.blackhorse_erp.module.employee.mapper.EmployeeMapper;
import com.inaing.blackhorse_erp.module.employee.service.IEmployeeService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetEmployeeUsecase {

    private final EmployeeMapper employeeMapper;
    private final IEmployeeService employeeService;

    public EmployeeResponseDto execute(String identifier) {
        Employee employee = employeeService.getByIdentifier(identifier);
        if (employee == null) {
            throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
        }

        return employeeMapper.toResponse(employee);
    }
}
