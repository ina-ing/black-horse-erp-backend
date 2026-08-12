package com.inaing.blackhorse_erp.module.inventory.dto.request;

import java.util.List;

import com.inaing.blackhorse_erp.module.inventory.domain.enums.LocationType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record InventoryAdjustmentRequestDto(

        @NotNull LocationType locationType,
        @NotBlank String referenceId,
        @NotEmpty @Valid List<InventoryAdjustmentItemRequestDto> items) {

}
