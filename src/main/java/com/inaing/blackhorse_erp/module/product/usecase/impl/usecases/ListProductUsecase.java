package com.inaing.blackhorse_erp.module.product.usecase.impl.usecases;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.module.product.dto.response.ProductResponseDto;
import com.inaing.blackhorse_erp.module.product.mapper.ProductMapper;
import com.inaing.blackhorse_erp.module.product.service.IProductService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ListProductUsecase {
    private final IProductService productService;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public List<ProductResponseDto> execute() {
        return productService.getAll().stream()
                .map(productMapper::toResponse)
                .toList();
    }
}
