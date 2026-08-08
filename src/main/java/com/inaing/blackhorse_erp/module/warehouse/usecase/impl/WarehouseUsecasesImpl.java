package com.inaing.blackhorse_erp.module.warehouse.usecase.impl;

import org.springframework.stereotype.Component;

import com.inaing.blackhorse_erp.module.warehouse.dto.request.WarehouseRequestDto;
import com.inaing.blackhorse_erp.module.warehouse.dto.response.WarehouseResponseDto;
import com.inaing.blackhorse_erp.module.warehouse.usecase.IWarehouseUsecases;
import com.inaing.blackhorse_erp.module.warehouse.usecase.impl.usecases.CreateWarehouseUsecase;
import com.inaing.blackhorse_erp.module.warehouse.usecase.impl.usecases.GetWarehouseUsecase;
import com.inaing.blackhorse_erp.module.warehouse.usecase.impl.usecases.UpdateWarehouseUsecase;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WarehouseUsecasesImpl implements IWarehouseUsecases {

    private final CreateWarehouseUsecase createWarehouseUsecase;
    private final UpdateWarehouseUsecase updateWarehouseUsecase;
    private final GetWarehouseUsecase getWarehouseUsecase;

    @Override
    public WarehouseResponseDto create(WarehouseRequestDto request) {
       return createWarehouseUsecase.execute(request);
    }

    @Override
    public WarehouseResponseDto update(String identifier, WarehouseRequestDto request) {
        return updateWarehouseUsecase.execute(identifier, request);
    }

    @Override
    public WarehouseResponseDto getByIdentifier(String identifier) {
        return getWarehouseUsecase.execute(identifier);
    }

}
