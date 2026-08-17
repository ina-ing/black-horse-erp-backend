package com.inaing.blackhorse_erp.module.productionOrder.usecase;

import com.inaing.blackhorse_erp.module.productionOrder.dto.request.ProductionOrderCreationRequestDto;
import com.inaing.blackhorse_erp.common.dto.list.PagedListWithAnalytics;
import com.inaing.blackhorse_erp.module.productionOrder.dto.request.ProductionOrderQueryParams;
import com.inaing.blackhorse_erp.module.productionOrder.dto.response.ProductionOrderListResponseDto;
import com.inaing.blackhorse_erp.module.productionOrder.dto.response.ProductionOrderResponseDto;
import com.inaing.blackhorse_erp.module.productionOrder.dto.response.analytics.ProductionOrderBasicAnalyticsDto;

public interface IProductionOrderUsecases {

    ProductionOrderResponseDto create(ProductionOrderCreationRequestDto request);

    ProductionOrderResponseDto getByIdentifier(String code);

    PagedListWithAnalytics<ProductionOrderBasicAnalyticsDto, ProductionOrderListResponseDto> getAll(
            ProductionOrderQueryParams params);

    ProductionOrderResponseDto accept(String identifier);
}
