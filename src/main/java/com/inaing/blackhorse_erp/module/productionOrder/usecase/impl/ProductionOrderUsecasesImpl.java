package com.inaing.blackhorse_erp.module.productionOrder.usecase.impl;

import org.springframework.stereotype.Component;

import com.inaing.blackhorse_erp.module.productionOrder.dto.request.ProductionOrderCreationRequestDto;
import com.inaing.blackhorse_erp.common.dto.list.PagedListWithAnalytics;
import com.inaing.blackhorse_erp.module.productionOrder.dto.request.ProductionOrderQueryParams;
import com.inaing.blackhorse_erp.module.productionOrder.dto.response.ProductionOrderListResponseDto;
import com.inaing.blackhorse_erp.module.productionOrder.dto.response.ProductionOrderResponseDto;
import com.inaing.blackhorse_erp.module.productionOrder.dto.response.analytics.ProductionOrderBasicAnalyticsDto;
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
    public ProductionOrderResponseDto create(ProductionOrderCreationRequestDto request) {
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
    public PagedListWithAnalytics<ProductionOrderBasicAnalyticsDto, ProductionOrderListResponseDto> getAll(
            ProductionOrderQueryParams params) {
        return getAllProductionOrdersUsecase.execute(params);
    }
}
