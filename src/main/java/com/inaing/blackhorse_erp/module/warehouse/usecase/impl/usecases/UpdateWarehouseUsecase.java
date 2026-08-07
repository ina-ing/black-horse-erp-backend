package com.inaing.blackhorse_erp.module.warehouse.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.employee.service.IEmployeeService;
import com.inaing.blackhorse_erp.module.role.domain.Role;
import com.inaing.blackhorse_erp.module.warehouse.domain.Warehouse;
import com.inaing.blackhorse_erp.module.warehouse.dto.request.WarehouseUpdateRequestDto;
import com.inaing.blackhorse_erp.module.warehouse.dto.response.WarehouseResponseDto;
import com.inaing.blackhorse_erp.module.warehouse.mapper.WarehouseMapper;
import com.inaing.blackhorse_erp.module.warehouse.service.IWarehouseService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateWarehouseUsecase {

    private final WarehouseMapper warehouseMapper;
    private final IWarehouseService warehouseService;
    private final IEmployeeService employeeService;

    @Transactional
    public WarehouseResponseDto execute(String identifier, WarehouseUpdateRequestDto request) {

        Warehouse warehouse = warehouseService.getByIdentifier(identifier);
        if (warehouse == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "Warehouse not found " + identifier);
        }

        Employee manager = employeeService.getById(request.manager());
        if (manager == null || manager.getRole() != Role.WAREHOUSE) {
            throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
        }

        warehouseMapper.updateEntity(request, warehouse);
        warehouse.setManager(manager);

        return warehouseMapper.toResponse(warehouseService.update(warehouse));
    }
}
