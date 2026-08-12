package com.inaing.blackhorse_erp.module.product.dto.request;

import com.inaing.blackhorse_erp.module.product.domain.enums.ProductStatus;

import jakarta.validation.constraints.NotNull;

public record ProductStatusUpdateRequestDto(

        @NotNull ProductStatus status) {

}
