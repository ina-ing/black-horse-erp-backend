package com.inaing.blackhorse_erp.module.inventory.usecase;

import com.inaing.blackhorse_erp.module.inventory.domain.enums.LocationType;
import com.inaing.blackhorse_erp.module.inventory.dto.request.InventoryAdjustmentRequestDto;
import com.inaing.blackhorse_erp.module.inventory.dto.response.InventoryResponseDto;

public interface IInventoryUsecases {

    InventoryResponseDto adjust(InventoryAdjustmentRequestDto request);

    InventoryResponseDto getByLocation(LocationType locationType, String referenceId);
}
