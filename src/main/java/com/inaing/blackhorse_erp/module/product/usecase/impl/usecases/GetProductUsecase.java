package com.inaing.blackhorse_erp.module.product.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.product.domain.Product;
import com.inaing.blackhorse_erp.module.product.dto.response.ProductResponseDto;
import com.inaing.blackhorse_erp.module.product.mapper.ProductMapper;
import com.inaing.blackhorse_erp.module.product.service.IProductService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetProductUsecase {

    private final IProductService productService;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public ProductResponseDto execute(String identifier) {
        Product product = productService.getByIdentifier(identifier);

        if (product == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "No product with : " + identifier);
        }
        return productMapper.toResponse(product);
    }

}
