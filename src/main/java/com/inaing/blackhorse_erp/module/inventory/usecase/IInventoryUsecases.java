package com.inaing.blackhorse_erp.module.inventory.usecase;

import java.util.List;

import com.inaing.blackhorse_erp.module.inventory.domain.enums.LocationType;
import com.inaing.blackhorse_erp.module.inventory.dto.request.InventoryAdjustmentRequestDto;
import com.inaing.blackhorse_erp.module.inventory.dto.response.InventoryItemResponseDto;
import com.inaing.blackhorse_erp.module.inventory.dto.response.InventoryResponseDto;

public interface IInventoryUsecases {

    InventoryResponseDto adjust(InventoryAdjustmentRequestDto request);

    InventoryResponseDto getByLocation(LocationType locationType, String referenceId);

    List<InventoryItemResponseDto> getItemsByLocationAndProduct(LocationType locationType, String referenceId,
            String productId);
}
