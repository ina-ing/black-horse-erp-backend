package com.inaing.blackhorse_erp.module.employee.usecase.impl.usecases;

import java.util.List;

import org.springframework.stereotype.Component;

import com.inaing.blackhorse_erp.module.employee.dto.EmployeeResponseDto;
import com.inaing.blackhorse_erp.module.employee.mapper.EmployeeMapper;
import com.inaing.blackhorse_erp.module.employee.service.IEmployeeService;
import com.inaing.blackhorse_erp.module.role.domain.Role;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetEmployeesByRoleUseCase {

    private final IEmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    public List<EmployeeResponseDto> execute(Role role) {
        return employeeService.getByRole(role)
                .stream()
                .map(employeeMapper::toResponse)
                .toList();
    }
}
