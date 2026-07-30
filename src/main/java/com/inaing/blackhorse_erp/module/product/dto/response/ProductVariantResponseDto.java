package com.inaing.blackhorse_erp.module.product.dto.response;

import java.util.List;

public record ProductVariantResponseDto(
        String id,
        String color,
        List<ProductVariantSizeResponseDto> availableSizes) {

}
