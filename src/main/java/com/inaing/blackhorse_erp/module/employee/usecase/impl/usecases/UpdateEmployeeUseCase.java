package com.inaing.blackhorse_erp.module.employee.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.employee.domain.EmployeeStatus;
import com.inaing.blackhorse_erp.module.employee.dto.EmployeeResponseDto;
import com.inaing.blackhorse_erp.module.employee.dto.request.EmployeeUpdateRequestDto;
import com.inaing.blackhorse_erp.module.employee.mapper.EmployeeMapper;
import com.inaing.blackhorse_erp.module.employee.service.IEmployeeService;
import com.inaing.blackhorse_erp.module.role.domain.Role;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateEmployeeUseCase {

    private final IEmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    @Transactional
    public EmployeeResponseDto execute(String identifier, EmployeeUpdateRequestDto request) {
        Employee employee = employeeService.getByIdentifier(identifier);
        if (employee == null) {
            throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND, "Employee not found " + identifier);
        }

        if (request.phone() != null
                && !request.phone().equals(employee.getPhone())
                && employeeService.findByPhone(request.phone()) != null) {
            throw new AppException(ErrorCode.DUPLICATE_PHONE,
                    "Phone number already registered " + request.phone());
        }

        if (request.role() != null) {
            Role role = Role.fromName(request.role());
            if (role == null) {
                throw new AppException(ErrorCode.ROLE_NOT_FOUND, "Role not found " + request.role());
            }
            employee.setRole(role);
        }

        if (request.status() != null) {
            EmployeeStatus status = EmployeeStatus.fromName(request.status());
            if (status == null) {
                throw new AppException(ErrorCode.INVALID_ENUM_VALUE, "Status not found " + request.status());
            }
            employee.setStatus(status);
        }

        employeeMapper.updateEntity(request, employee);

        return employeeMapper.toResponse(employeeService.update(employee));
    }
}
