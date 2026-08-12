package com.inaing.blackhorse_erp.module.supply.dto.request;

import com.inaing.blackhorse_erp.module.supply.domain.enums.SupplyStatus;

import jakarta.validation.constraints.NotNull;

public record SupplyStatusUpdateRequestDto(

        @NotNull SupplyStatus status) {

}
