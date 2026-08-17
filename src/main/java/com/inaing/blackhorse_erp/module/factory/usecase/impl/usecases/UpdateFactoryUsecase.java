package com.inaing.blackhorse_erp.module.factory.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.factory.domain.Factory;
import com.inaing.blackhorse_erp.module.factory.dto.request.FactoryUpdateRequestDto;
import com.inaing.blackhorse_erp.module.factory.dto.response.FactoryResponseDto;
import com.inaing.blackhorse_erp.module.factory.mapper.FactoryMapper;
import com.inaing.blackhorse_erp.module.factory.service.IFactoryService;

import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityAction;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityEntityType;
import com.inaing.blackhorse_erp.module.activityLog.service.IActivityLogService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateFactoryUsecase {

    private final FactoryMapper factoryMapper;
    private final IFactoryService factoryService;
    private final IActivityLogService activityLogService;

    @Transactional
    public FactoryResponseDto execute(String identifier, FactoryUpdateRequestDto request) {
        Factory factory = factoryService.getByIdentifier(identifier);
        if (factory == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "Factory not found " + identifier);
        }

        factoryMapper.updateEntity(request, factory);

        Factory updated = factoryService.update(factory);

        activityLogService.record(
                ActivityAction.FACTORY_UPDATED,
                "Updated factory " + updated.getName() + ".",
                ActivityEntityType.FACTORY, updated.getId(), updated.getCode(),
                ActionTrigger.MANUAL);

        return factoryMapper.toResponse(updated);
    }
}
