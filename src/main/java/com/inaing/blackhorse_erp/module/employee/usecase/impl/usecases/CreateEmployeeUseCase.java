package com.inaing.blackhorse_erp.module.employee.usecase.impl.usecases;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.employee.dto.EmployeeResponseDto;
import com.inaing.blackhorse_erp.module.employee.dto.request.EmployeeCreationRequestDto;
import com.inaing.blackhorse_erp.module.employee.mapper.EmployeeMapper;
import com.inaing.blackhorse_erp.module.employee.service.IEmployeeService;

import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityAction;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityEntityType;
import com.inaing.blackhorse_erp.module.activityLog.service.IActivityLogService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateEmployeeUseCase {

    private final IEmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    private final PasswordEncoder passwordEncoder;
    private final IActivityLogService activityLogService;

    @Transactional
    public EmployeeResponseDto execute(EmployeeCreationRequestDto request) {
        Employee employee = employeeMapper.toEntity(request);
        employee.setPasswordHash(passwordEncoder.encode(request.password()));
        
        Employee created = employeeService.create(employee);

        activityLogService.record(
                ActivityAction.EMPLOYEE_CREATED,
                "Created " + created.getRole() + " employee " + created.getFullname()
                        + " (" + created.getCode() + ").",
                ActivityEntityType.EMPLOYEE, created.getId(), created.getCode(),
                ActionTrigger.CREATION);

        return employeeMapper.toResponse(created);
    }
}
          