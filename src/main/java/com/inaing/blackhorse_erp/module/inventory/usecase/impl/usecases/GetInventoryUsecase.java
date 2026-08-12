package com.inaing.blackhorse_erp.module.inventory.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.module.inventory.domain.Inventory;
import com.inaing.blackhorse_erp.module.inventory.domain.enums.LocationType;
import com.inaing.blackhorse_erp.module.inventory.dto.response.InventoryResponseDto;
import com.inaing.blackhorse_erp.module.inventory.mapper.InventoryMapper;
import com.inaing.blackhorse_erp.module.inventory.service.IInventoryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetInventoryUsecase {

    private final IInventoryService inventoryService;
    private final InventoryMapper inventoryMapper;

    @Transactional(readOnly = true)
    public InventoryResponseDto execute(LocationType locationType, String referenceId) {
        Inventory inventory = inventoryService.getByLocation(locationType, referenceId);
        return inventoryMapper.toResponse(inventory);
    }
}
