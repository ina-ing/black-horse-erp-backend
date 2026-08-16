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

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateFactoryUsecase {

    private final FactoryMapper factoryMapper;
    private final IFactoryService factoryService;

    @Transactional
    public FactoryResponseDto execute(String identifier, FactoryUpdateRequestDto request) {
        Factory factory = factoryService.getByIdentifier(identifier);
        if (factory == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "Factory not found " + identifier);
        }

        factoryMapper.updateEntity(request, factory);

        return factoryMapper.toResponse(factoryService.update(factory));
    }
}
