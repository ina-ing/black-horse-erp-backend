package com.inaing.blackhorse_erp.module.product.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record ProductVariantCreationRequestDto(
        @NotBlank String color,
        @NotEmpty List<String> availableSizes) {

}
