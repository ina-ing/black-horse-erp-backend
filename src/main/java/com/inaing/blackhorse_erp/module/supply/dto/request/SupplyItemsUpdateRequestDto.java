package com.inaing.blackhorse_erp.module.supply.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record SupplyItemsUpdateRequestDto(

        @NotEmpty @Valid List<SupplyItemRequestDto> items) {

}
