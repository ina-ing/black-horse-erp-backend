package com.inaing.blackhorse_erp.module.product.dto.request;

import com.inaing.blackhorse_erp.module.product.domain.enums.Gender;
import com.inaing.blackhorse_erp.module.product.domain.enums.ProductStatus;
import com.inaing.blackhorse_erp.module.product.domain.enums.SizeSystem;
import com.inaing.blackhorse_erp.module.product.dto.response.MaterialDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductUpdateRequestDto(
        @NotBlank @Size(max = 20) String articleCode,
        @Size(max = 100) String name,
        @NotBlank String category,
        @NotNull Gender gender,
        @NotNull SizeSystem sizeSystem,
        @NotNull ProductStatus status,
        @NotNull @Valid MaterialDto material) {

}
