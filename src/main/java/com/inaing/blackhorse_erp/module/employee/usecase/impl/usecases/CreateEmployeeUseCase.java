package com.inaing.blackhorse_erp.module.employee.usecase.impl.usecases;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.employee.dto.EmployeeCreationRequestDto;
import com.inaing.blackhorse_erp.module.employee.dto.EmployeeResponseDto;
import com.inaing.blackhorse_erp.module.employee.mapper.EmployeeMapper;
import com.inaing.blackhorse_erp.module.employee.service.IEmployeeService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateEmployeeUseCase {

    private final IEmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public EmployeeResponseDto execute(EmployeeCreationRequestDto request) {
        Employee employee = employeeMapper.toEntity(request);
        employee.setPasswordHash(passwordEncoder.encode(request.password()));
        
        return employeeMapper.toResponse(employeeService.create(employee));
    }
}
          