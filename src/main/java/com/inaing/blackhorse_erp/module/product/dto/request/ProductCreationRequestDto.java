package com.inaing.blackhorse_erp.module.product.dto.request;

import java.util.List;

import com.inaing.blackhorse_erp.module.product.domain.enums.Gender;
import com.inaing.blackhorse_erp.module.product.domain.enums.SizeSystem;
import com.inaing.blackhorse_erp.module.product.dto.response.MaterialDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductCreationRequestDto(
        @NotBlank @Size(max = 20) String articleCode,
        @Size(max = 100) String name,
        @NotBlank String category,
        @NotNull Gender gender,
        @NotNull MaterialDto material,
        @NotNull SizeSystem sizeSystem,
        @Valid @NotEmpty(message = "At least one variant is required") List<ProductVariantCreationRequestDto> variants) {
}
