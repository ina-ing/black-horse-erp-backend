package com.inaing.blackhorse_erp.module.product.dto.response;

import java.util.List;

import com.inaing.blackhorse_erp.module.product.domain.enums.ProductStatus;

public record ProductVariantResponseDto(
        String id,
        String color,
        String imageUrl,
        ProductStatus status,
        List<ProductVariantSizeResponseDto> availableSizes) {

}
