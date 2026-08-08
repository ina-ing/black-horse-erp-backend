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
public class CreateFactoryUsecase {
    
    private final FactoryMapper factoryMapper;
    private final IFactoryService factoryService;
    private final IEmployeeService employeeService;

    @Transactional
    public FactoryResponseDto execute(FactoryRequestDto request){

        Employee manager = employeeService.getByIdentifier(request.manager());
        if(manager == null || manager.getRole() != Role.FACTORY){
            throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
        }

        Factory factory = factoryMapper.toEntity(request);
        factory.setManager(manager);
        return factoryMapper.toResponse(factoryService.create(factory));
    }
}
