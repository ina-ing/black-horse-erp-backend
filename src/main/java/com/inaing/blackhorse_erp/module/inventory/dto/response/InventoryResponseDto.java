package com.inaing.blackhorse_erp.module.inventory.dto.response;

import java.util.List;

import com.inaing.blackhorse_erp.module.inventory.domain.enums.LocationType;

public record InventoryResponseDto(

        String id,
        LocationType locationType,
        String reference,
        List<InventoryItemResponseDto> items) {

}
