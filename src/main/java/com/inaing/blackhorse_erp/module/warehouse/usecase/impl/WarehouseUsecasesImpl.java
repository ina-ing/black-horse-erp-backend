package com.inaing.blackhorse_erp.module.warehouse.usecase.impl;

import org.springframework.stereotype.Component;

import com.inaing.blackhorse_erp.module.warehouse.dto.request.WarehouseCreationRequestDto;
import com.inaing.blackhorse_erp.module.warehouse.dto.request.WarehouseUpdateRequestDto;
import com.inaing.blackhorse_erp.module.warehouse.dto.response.WarehouseResponseDto;
import com.inaing.blackhorse_erp.module.warehouse.usecase.IWarehouseUsecases;
import com.inaing.blackhorse_erp.module.warehouse.usecase.impl.usecases.CreateWarehouseUsecase;
import com.inaing.blackhorse_erp.module.warehouse.usecase.impl.usecases.GetWarehouse;
import com.inaing.blackhorse_erp.module.warehouse.usecase.impl.usecases.UpdateWarehouseUsecase;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WarehouseUsecasesImpl implements IWarehouseUsecases {

    private final CreateWarehouseUsecase createWarehouseUsecase;
    private final UpdateWarehouseUsecase updateWarehouseUsecase;
    private final GetWarehouse getWarehouse;

    @Override
    public WarehouseResponseDto create(WarehouseCreationRequestDto request) {
       return createWarehouseUsecase.execute(request);
    }

    @Override
    public WarehouseResponseDto update(String identifier, WarehouseUpdateRequestDto request) {
        return updateWarehouseUsecase.execute(identifier, request);
    }

    @Override
    public WarehouseResponseDto getByIdentifier(String identifier) {
        return getWarehouse.execute(identifier);
    }

}
