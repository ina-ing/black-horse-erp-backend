package com.inaing.blackhorse_erp.module.factory.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.backlog.service.IBacklogService;
import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.employee.service.IEmployeeService;
import com.inaing.blackhorse_erp.module.factory.domain.Factory;
import com.inaing.blackhorse_erp.module.factory.dto.request.FactoryRequestDto;
import com.inaing.blackhorse_erp.module.factory.dto.response.FactoryResponseDto;
import com.inaing.blackhorse_erp.module.factory.mapper.FactoryMapper;
import com.inaing.blackhorse_erp.module.factory.service.IFactoryService;
import com.inaing.blackhorse_erp.module.inventory.domain.enums.LocationType;
import com.inaing.blackhorse_erp.module.inventory.service.IInventoryService;
import com.inaing.blackhorse_erp.module.role.domain.Role;

import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityAction;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityEntityType;
import com.inaing.blackhorse_erp.module.activityLog.service.IActivityLogService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateFactoryUsecase {

    private final FactoryMapper factoryMapper;
    private final IFactoryService factoryService;
    private final IEmployeeService employeeService;
    private final IInventoryService inventoryService;
    private final IBacklogService productionBacklogService;
    private final IActivityLogService activityLogService;

    @Transactional
    public FactoryResponseDto execute(FactoryRequestDto request) {

        Employee manager = employeeService.getByIdentifier(request.manager());
        if (manager == null || manager.getRole() != Role.FACTORY) {
            throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
        }

        Factory factory = factoryMapper.toEntity(request);
        factory.setManager(manager);

        Factory createdFactory = factoryService.create(factory);
        productionBacklogService.create(createdFactory);

        inventoryService.createFor(LocationType.FACTORY, createdFactory.getId());
        activityLogService.record(
                ActivityAction.FACTORY_CREATED,
                "Created factory " + createdFactory.getName() + " managed by "
                        + manager.getFullname() + ".",
                ActivityEntityType.FACTORY, createdFactory.getId(), createdFactory.getCode(),
                ActionTrigger.CREATION);

        return factoryMapper.toResponse(createdFactory);
    }
}
