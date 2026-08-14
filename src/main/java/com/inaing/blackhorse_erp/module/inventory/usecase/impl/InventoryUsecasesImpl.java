package com.inaing.blackhorse_erp.module.inventory.usecase.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.inaing.blackhorse_erp.module.inventory.domain.enums.LocationType;
import com.inaing.blackhorse_erp.module.inventory.dto.request.InventoryAdjustmentRequestDto;
import com.inaing.blackhorse_erp.module.inventory.dto.response.InventoryItemResponseDto;
import com.inaing.blackhorse_erp.module.inventory.dto.response.InventoryResponseDto;
import com.inaing.blackhorse_erp.module.inventory.usecase.IInventoryUsecases;
import com.inaing.blackhorse_erp.module.inventory.usecase.impl.usecases.AdjustInventoryUsecase;
import com.inaing.blackhorse_erp.module.inventory.usecase.impl.usecases.GetInventoryItemsByProductUsecase;
import com.inaing.blackhorse_erp.module.inventory.usecase.impl.usecases.GetInventoryUsecase;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InventoryUsecasesImpl implements IInventoryUsecases {

    private final AdjustInventoryUsecase adjustInventoryUsecase;
    private final GetInventoryUsecase getInventoryUsecase;
    private final GetInventoryItemsByProductUsecase getInventoryItemsByProductUsecase;

    @Override
    public InventoryResponseDto adjust(InventoryAdjustmentRequestDto request) {
        return adjustInventoryUsecase.execute(request);
    }

    @Override
    public InventoryResponseDto getByLocation(LocationType locationType, String referenceId) {
        return getInventoryUsecase.execute(locationType, referenceId);
    }

    @Override
    public List<InventoryItemResponseDto> getItemsByLocationAndProduct(LocationType locationType, String referenceId,
            String productId) {
        return getInventoryItemsByProductUsecase.execute(locationType, referenceId, productId);
    }
}
