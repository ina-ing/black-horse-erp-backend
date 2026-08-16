package com.inaing.blackhorse_erp.module.product.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.exception.exceptions.BusinessRuleException;
import com.inaing.blackhorse_erp.module.product.domain.Product;
import com.inaing.blackhorse_erp.module.product.domain.enums.ProductStatus;
import com.inaing.blackhorse_erp.module.product.dto.request.ProductStatusUpdateRequestDto;
import com.inaing.blackhorse_erp.module.product.dto.response.ProductResponseDto;
import com.inaing.blackhorse_erp.module.product.mapper.ProductMapper;
import com.inaing.blackhorse_erp.module.product.service.IProductService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateProductStatusUsecase {

    private final ProductMapper productMapper;
    private final IProductService productService;

    @Transactional
    public ProductResponseDto execute(String id, ProductStatusUpdateRequestDto request) {

        Product product = productService.getById(id);

        if (request.status() != ProductStatus.ACTIVE && request.status() != ProductStatus.INACTIVE
                || ProductStatus.fromName(request.status().toString()) == null) {

            throw new BusinessRuleException(
                    "INVALID_STATUS_VALUE",
                    "Product status can only be updated to active or inactive.");

        }
        product.setStatus(request.status());
        return productMapper.toResponse(productService.update(product));
    }
}
