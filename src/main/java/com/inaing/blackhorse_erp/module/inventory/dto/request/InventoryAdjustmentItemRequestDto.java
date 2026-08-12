package com.inaing.blackhorse_erp.module.inventory.dto.request;

import com.inaing.blackhorse_erp.common.dto.request.VariantQuantityRequestDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InventoryAdjustmentItemRequestDto(

        @NotBlank String variantSizeId,
        @NotNull @Min(0) Integer quantity) implements VariantQuantityRequestDto {

}
