package com.inaing.blackhorse_erp.module.factory.usecase.impl;

import org.springframework.stereotype.Component;

import com.inaing.blackhorse_erp.module.factory.dto.request.FactoryRequestDto;
import com.inaing.blackhorse_erp.module.factory.dto.response.FactoryResponseDto;
import com.inaing.blackhorse_erp.module.factory.usecase.IFactoryUsecases;
import com.inaing.blackhorse_erp.module.factory.usecase.impl.usecases.CreateFactoryUsecase;
import com.inaing.blackhorse_erp.module.factory.usecase.impl.usecases.GetFactoryUsecase;
import com.inaing.blackhorse_erp.module.factory.usecase.impl.usecases.UpdateFactoryUsecase;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FactoryUsecasesImpl implements IFactoryUsecases {

    private final CreateFactoryUsecase createFactoryUsecase;
    private final UpdateFactoryUsecase updateFactoryUsecase;
    private final GetFactoryUsecase getFactoryUsecase;

    @Override
    public FactoryResponseDto create(FactoryRequestDto request) {
        return createFactoryUsecase.execute(request);
    }

    @Override
    public FactoryResponseDto update(String identifier, FactoryRequestDto request) {
       return updateFactoryUsecase.execute(identifier, request);
    }

    @Override
    public FactoryResponseDto getByIdentifier(String identifier) {
        return getFactoryUsecase.execute(identifier);
    }

}
