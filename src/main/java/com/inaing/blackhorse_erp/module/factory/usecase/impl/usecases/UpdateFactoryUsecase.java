package com.inaing.blackhorse_erp.module.factory.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.employee.service.IEmployeeService;
import com.inaing.blackhorse_erp.module.factory.domain.Factory;
import com.inaing.blackhorse_erp.module.factory.dto.request.FactoryRequestDto;
import com.inaing.blackhorse_erp.module.factory.dto.response.FactoryResponseDto;
import com.inaing.blackhorse_erp.module.factory.mapper.FactoryMapper;
import com.inaing.blackhorse_erp.module.factory.service.IFactoryService;
import com.inaing.blackhorse_erp.module.role.domain.Role;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateFactoryUsecase {

    private final FactoryMapper factoryMapper;
    private final IFactoryService factoryService;
    private final IEmployeeService employeeService;

    @Transactional
    public FactoryResponseDto execute(String identifier, FactoryRequestDto request) {
        Factory factory = factoryService.getByIdentifier(identifier);
        if (factory == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "Factory not found " + identifier);
        }

        Employee manager = employeeService.getById(request.manager());
        if (manager == null || manager.getRole() != Role.FACTORY) {
            throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
        }

        factoryMapper.updateEntity(request, factory);
        factory.setManager(manager);

        return factoryMapper.toResponse(factoryService.update(factory));
    }
}
