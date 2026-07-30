package com.inaing.blackhorse_erp.module.product.usecase.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.inaing.blackhorse_erp.module.product.dto.request.ProductCreationRequestDto;
import com.inaing.blackhorse_erp.module.product.dto.request.ProductUpdateRequestDto;
import com.inaing.blackhorse_erp.module.product.dto.response.ProductResponseDto;
import com.inaing.blackhorse_erp.module.product.usecase.IProductUsecase;
import com.inaing.blackhorse_erp.module.product.usecase.impl.usecases.CreateProductUsecase;
import com.inaing.blackhorse_erp.module.product.usecase.impl.usecases.GetProductUsecase;
import com.inaing.blackhorse_erp.module.product.usecase.impl.usecases.ListProductUsecase;
import com.inaing.blackhorse_erp.module.product.usecase.impl.usecases.UpdateProductUsecase;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductUsecaseImpl implements IProductUsecase {

    private final CreateProductUsecase createProductUsecase;
    private final GetProductUsecase getProductUsecase;
    private final ListProductUsecase listProductUsecase;
    private final UpdateProductUsecase updateProductUsecase;

    @Override
    public ProductResponseDto create(ProductCreationRequestDto request) {
        return createProductUsecase.execute(request);
    }

    @Override
    public ProductResponseDto getByIdentifier(String identifier) {
        return getProductUsecase.execute(identifier);
    }

    @Override
    public List<ProductResponseDto> getAll() {
        return listProductUsecase.execute();
    }

    @Override
    public ProductResponseDto update(String id, ProductUpdateRequestDto request) {
        return updateProductUsecase.execute(id, request);
    }

}
