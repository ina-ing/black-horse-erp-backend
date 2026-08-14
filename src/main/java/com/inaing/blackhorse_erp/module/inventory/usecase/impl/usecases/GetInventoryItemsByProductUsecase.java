package com.inaing.blackhorse_erp.module.inventory.usecase.impl.usecases;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.module.inventory.domain.Inventory;
import com.inaing.blackhorse_erp.module.inventory.domain.enums.LocationType;
import com.inaing.blackhorse_erp.module.inventory.dto.response.InventoryItemResponseDto;
import com.inaing.blackhorse_erp.module.inventory.mapper.InventoryMapper;
import com.inaing.blackhorse_erp.module.inventory.service.IInventoryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetInventoryItemsByProductUsecase {

    private final IInventoryService inventoryService;
    private final InventoryMapper inventoryMapper;

    @Transactional(readOnly = true)
    public List<InventoryItemResponseDto> execute(LocationType locationType, String referenceId, String productId) {
        Inventory inventory = inventoryService.getByLocation(locationType, referenceId);
        if (inventory == null) {
            return List.of();
        }

        return inventory.getItems().stream()
                .filter(item -> item.getVariantSize().getProductVariant().getProduct().getId().equals(productId))
                .map(inventoryMapper::toItemResponse)
                .toList();
    }
}
