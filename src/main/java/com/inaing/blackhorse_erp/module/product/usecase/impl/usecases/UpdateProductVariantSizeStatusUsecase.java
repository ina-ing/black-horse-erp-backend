package com.inaing.blackhorse_erp.module.product.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.exception.exceptions.BusinessRuleException;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariantSize;
import com.inaing.blackhorse_erp.module.product.domain.enums.ProductStatus;
import com.inaing.blackhorse_erp.module.product.dto.request.ProductStatusUpdateRequestDto;
import com.inaing.blackhorse_erp.module.product.dto.response.ProductVariantSizeResponseDto;
import com.inaing.blackhorse_erp.module.product.mapper.ProductMapper;
import com.inaing.blackhorse_erp.module.product.service.IProductVariantSizeService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateProductVariantSizeStatusUsecase {

    private final ProductMapper productMapper;
    private final IProductVariantSizeService productVariantSizeService;

    @Transactional
    public ProductVariantSizeResponseDto execute(String id, ProductStatusUpdateRequestDto request) {

        ProductVariantSize variantSize = productVariantSizeService.getById(id);

        if (request.status() != ProductStatus.ACTIVE && request.status() != ProductStatus.INACTIVE) {
            throw new BusinessRuleException(
                    "INVALID_STATUS_VALUE",
                    "Product variant size status can only be updated to active or inactive.");
        }

        variantSize.setStatus(request.status());
        return productMapper.toVariantSizeResponse(productVariantSizeService.update(variantSize));
    }
}
