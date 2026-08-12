package com.inaing.blackhorse_erp.module.product.dto.response;

import com.inaing.blackhorse_erp.module.product.domain.enums.ProductStatus;

public record ProductVariantSizeResponseDto(
        String id,
        String size,
        String sku,
        ProductStatus status) {

}
