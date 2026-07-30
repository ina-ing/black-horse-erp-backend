package com.inaing.blackhorse_erp.module.product.dto.response;

import java.util.List;

import com.inaing.blackhorse_erp.module.product.domain.enums.Gender;
import com.inaing.blackhorse_erp.module.product.domain.enums.ProductStatus;
import com.inaing.blackhorse_erp.module.product.domain.enums.SizeSystem;

public record ProductResponseDto(
                String id,
                String articleCode,
                String name,
                String category,
                Gender gender,
                ProductStatus status,
                SizeSystem sizeSystem,
                MaterialDto material,
                List<ProductVariantResponseDto> variants) {

}
