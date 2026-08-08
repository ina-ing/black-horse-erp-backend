package com.inaing.blackhorse_erp.module.productionOrder.dto.request;

import com.inaing.blackhorse_erp.common.dto.request.VariantQuantityRequestDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductionOrderItemRequestDto(

        @NotBlank String variantSizeId,
        @NotNull @Positive @Min(value = 1) Integer quantity)
        implements VariantQuantityRequestDto {

}
