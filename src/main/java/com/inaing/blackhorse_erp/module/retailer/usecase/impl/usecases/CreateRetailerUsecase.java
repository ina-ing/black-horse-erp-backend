package com.inaing.blackhorse_erp.module.retailer.usecase.impl.usecases;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.exception.exceptions.BusinessRuleException;
import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.employee.service.IEmployeeService;
import com.inaing.blackhorse_erp.module.retailer.domain.Retailer;
import com.inaing.blackhorse_erp.module.retailer.dto.request.RetailerCreationRequestDto;
import com.inaing.blackhorse_erp.module.retailer.dto.response.RetailerResponseDto;
import com.inaing.blackhorse_erp.module.retailer.mapper.RetailerMapper;
import com.inaing.blackhorse_erp.module.retailer.service.IRetailerService;
import com.inaing.blackhorse_erp.module.role.domain.Role;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateRetailerUsecase {

    private final IEmployeeService employeeService;
    private final IRetailerService retailerService;
    private final RetailerMapper retailerMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RetailerResponseDto execute(RetailerCreationRequestDto request) {
        Employee salesman = employeeService.getById(request.assignedSalesman());

        if (salesman == null || salesman.getRole() != Role.SALES) {
            throw new BusinessRuleException("ASSIGNED_SALESMAN_NOT_FOUND",
                    "The specified salesman doesn't exist: " + request.assignedSalesman());
        }

        Retailer retailer = retailerMapper.toEntity(request);
        retailer.setAssignedSalesman(salesman);
        retailer.setPasswordHash(passwordEncoder.encode(request.password()));
        return retailerMapper.toResponse(retailerService.create(retailer));
    }
}
