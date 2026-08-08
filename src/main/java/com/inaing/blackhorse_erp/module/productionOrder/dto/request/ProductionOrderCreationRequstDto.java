package com.inaing.blackhorse_erp.module.productionOrder.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record ProductionOrderCreationRequstDto(

        @NotBlank String warehouse,
        @NotEmpty @Valid List<ProductionOrderItemRequestDto> items) {

}
