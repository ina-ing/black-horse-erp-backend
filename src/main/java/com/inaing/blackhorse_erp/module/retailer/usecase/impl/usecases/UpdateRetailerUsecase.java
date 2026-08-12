package com.inaing.blackhorse_erp.module.retailer.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.employee.service.IEmployeeService;
import com.inaing.blackhorse_erp.module.retailer.domain.Retailer;
import com.inaing.blackhorse_erp.module.retailer.dto.request.RetailerUpdateRequestDto;
import com.inaing.blackhorse_erp.module.retailer.dto.response.RetailerResponseDto;
import com.inaing.blackhorse_erp.module.retailer.mapper.RetailerMapper;
import com.inaing.blackhorse_erp.module.retailer.service.IRetailerService;
import com.inaing.blackhorse_erp.module.role.domain.Role;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateRetailerUsecase {

    private final IRetailerService retailerService;
    private final IEmployeeService employeeService;
    private final RetailerMapper retailerMapper;

    @Transactional
    public RetailerResponseDto execute(String identifier, RetailerUpdateRequestDto request) {
        Retailer retailer = retailerService.getByIdentifier(identifier);
        if (retailer == null) {
            throw new AppException(ErrorCode.RETAILER_NOT_FOUND, "Retailer not found " + identifier);
        }

        if (request.phone() != null
                && !request.phone().equals(retailer.getPhone())
                && retailerService.findByPhone(request.phone()) != null) {
            throw new AppException(ErrorCode.DUPLICATE_PHONE,
                    "Phone number already registered " + request.phone());
        }

        if (request.assignedSalesman() != null) {
            Employee salesman = employeeService.getById(request.assignedSalesman());
            if (salesman == null || salesman.getRole() != Role.SALES) {
                throw new AppException(ErrorCode.ASSIGNED_SALESMAN_NOT_FOUND,
                        "The specified salesman doesn't exist: " + request.assignedSalesman());
            }
            retailer.setAssignedSalesman(salesman);
        }

        retailerMapper.updateEntity(request, retailer);

        return retailerMapper.toResponse(retailerService.update(retailer));
    }
}
