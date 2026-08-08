package com.inaing.blackhorse_erp.module.productionOrder.usecase.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.inaing.blackhorse_erp.module.productionOrder.dto.request.ProductionOrderCreationRequstDto;
import com.inaing.blackhorse_erp.module.productionOrder.dto.response.ProductionOrderResponseDto;
import com.inaing.blackhorse_erp.module.productionOrder.usecase.IProductionOrderUsecases;
import com.inaing.blackhorse_erp.module.productionOrder.usecase.impl.usecases.AcceptProductionOrderUsecase;
import com.inaing.blackhorse_erp.module.productionOrder.usecase.impl.usecases.CreateProductionOrderUsecase;
import com.inaing.blackhorse_erp.module.productionOrder.usecase.impl.usecases.GetAllProductionOrdersUsecase;
import com.inaing.blackhorse_erp.module.productionOrder.usecase.impl.usecases.GetProductionOrderUsecase;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductionOrderUsecasesImpl implements IProductionOrderUsecases {

    private final CreateProductionOrderUsecase createProductionOrderUsecase;
    private final AcceptProductionOrderUsecase acceptProductionOrderUsecase;
    private final GetProductionOrderUsecase getProductionOrderUsecase;
    private final GetAllProductionOrdersUsecase getAllProductionOrdersUsecase;

    @Override
    public ProductionOrderResponseDto create(ProductionOrderCreationRequstDto request) {
        return createProductionOrderUsecase.execute(request);
    }

    @Override
    public ProductionOrderResponseDto accept(String identifier) {
        return acceptProductionOrderUsecase.execute(identifier);
    }

    @Override
    public ProductionOrderResponseDto getByIdentifier(String code) {
        return getProductionOrderUsecase.execute(code);
    }

    @Override
    public List<ProductionOrderResponseDto> getAll() {
        return getAllProductionOrdersUsecase.execute();
    }
}
