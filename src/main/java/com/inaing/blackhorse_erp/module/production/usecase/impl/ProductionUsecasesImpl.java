package com.inaing.blackhorse_erp.module.production.usecase.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.inaing.blackhorse_erp.module.production.dto.request.ProductionRequestDto;
import com.inaing.blackhorse_erp.module.production.dto.response.ProductionResponseDto;
import com.inaing.blackhorse_erp.module.production.usecase.IProductionUsecases;
import com.inaing.blackhorse_erp.module.production.usecase.impl.usecases.CreateProductionUsecase;
import com.inaing.blackhorse_erp.module.production.usecase.impl.usecases.GetAllProductionUsecase;
import com.inaing.blackhorse_erp.module.production.usecase.impl.usecases.GetProductionUsecase;
import com.inaing.blackhorse_erp.module.production.usecase.impl.usecases.UpdateProductionUsecase;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductionUsecasesImpl implements IProductionUsecases {

    private final CreateProductionUsecase createProductionUsecase;
    private final GetProductionUsecase getProductionUsecase;
    private final GetAllProductionUsecase getAllProductionUsecase;
    private final UpdateProductionUsecase updateProductionUsecase;

    @Override
    public ProductionResponseDto create(ProductionRequestDto request) {
        return createProductionUsecase.execute(request);
    }

    @Override
    public ProductionResponseDto getByIdentifier(String identifier) {
        return getProductionUsecase.execute(identifier);
    }

    @Override
    public List<ProductionResponseDto> getAll() {
        return getAllProductionUsecase.execute();
    }

    @Override
    public ProductionResponseDto update(String identifier, ProductionRequestDto request) {
        return updateProductionUsecase.execute(identifier, request);
    }

}
