package com.inaing.blackhorse_erp.module.retailer.usecase.impl.usecases;

import org.springframework.stereotype.Component;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.employee.dto.EmployeeResponseDto;
import com.inaing.blackhorse_erp.module.employee.mapper.EmployeeMapper;
import com.inaing.blackhorse_erp.module.employee.service.IEmployeeService;
import com.inaing.blackhorse_erp.module.retailer.domain.Retailer;
import com.inaing.blackhorse_erp.module.retailer.service.IRetailerService;
import com.inaing.blackhorse_erp.module.role.domain.Role;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;
import com.inaing.blackhorse_erp.security.context.CurrentUserProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetAssignedSalesmanUsecase {

    private final EmployeeMapper employeeMapper;
    private final IRetailerService retailerService;
    private final IEmployeeService employeeService;
    private final CurrentUserProvider currentUserProvider;

    public EmployeeResponseDto execute() {

        AuthPrincipal principal = currentUserProvider.currentPrincipal()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        Retailer retailer = retailerService.getById(principal.id());
        if (retailer == null) {
            throw new AppException(ErrorCode.RETAILER_NOT_FOUND);
        }

        Employee employee = employeeService.getByIdentifier(retailer.getAssignedSalesman().getId());
        if (employee == null || !employee.getRole().equals(Role.SALES)) {
            throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
        }

        return employeeMapper.toResponse(employee);
    }
}
