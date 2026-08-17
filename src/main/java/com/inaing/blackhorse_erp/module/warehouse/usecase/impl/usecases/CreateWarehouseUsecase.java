package com.inaing.blackhorse_erp.module.warehouse.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.employee.service.IEmployeeService;
import com.inaing.blackhorse_erp.module.inventory.domain.enums.LocationType;
import com.inaing.blackhorse_erp.module.inventory.service.IInventoryService;
import com.inaing.blackhorse_erp.module.role.domain.Role;
import com.inaing.blackhorse_erp.module.warehouse.domain.Warehouse;
import com.inaing.blackhorse_erp.module.warehouse.dto.request.WarehouseRequestDto;
import com.inaing.blackhorse_erp.module.warehouse.dto.response.WarehouseResponseDto;
import com.inaing.blackhorse_erp.module.warehouse.mapper.WarehouseMapper;
import com.inaing.blackhorse_erp.module.warehouse.service.IWarehouseService;

import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityAction;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityEntityType;
import com.inaing.blackhorse_erp.module.activityLog.service.IActivityLogService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateWarehouseUsecase {

    private final WarehouseMapper warehouseMapper;
    private final IWarehouseService warehouseService;
    private final IEmployeeService employeeService;
    private final IInventoryService inventoryService;
    private final IActivityLogService activityLogService;

    @Transactional
    public WarehouseResponseDto execute(WarehouseRequestDto request) {

        Employee manager = employeeService.getById(request.manager());

        if (manager == null || manager.getRole() != Role.WAREHOUSE) {
            throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
        }

        Warehouse warehouse = warehouseMapper.toEntity(request);
        warehouse.setManager(manager);

        Warehouse createdWarehouse = warehouseService.create(warehouse);
        inventoryService.createFor(LocationType.WAREHOUSE, createdWarehouse.getId());

        activityLogService.record(
                ActivityAction.WAREHOUSE_CREATED,
                "Created warehouse " + createdWarehouse.getName() + " managed by "
                        + manager.getFullname() + ".",
                ActivityEntityType.WAREHOUSE, createdWarehouse.getId(), createdWarehouse.getCode(),
                ActionTrigger.CREATION);

        return warehouseMapper.toResponse(createdWarehouse);
    }
}
