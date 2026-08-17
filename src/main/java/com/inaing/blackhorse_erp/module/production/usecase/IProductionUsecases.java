package com.inaing.blackhorse_erp.module.production.usecase;

import java.util.List;

import com.inaing.blackhorse_erp.module.production.dto.request.ProductionRequestDto;
import com.inaing.blackhorse_erp.common.dto.list.PagedListWithAnalytics;
import com.inaing.blackhorse_erp.module.production.dto.request.ProductionQueryParams;
import com.inaing.blackhorse_erp.module.production.dto.response.ProductionListResponseDto;
import com.inaing.blackhorse_erp.module.production.dto.response.ProductionResponseDto;
import com.inaing.blackhorse_erp.module.production.dto.response.analytics.ProductionBasicAnalyticsDto;

public interface IProductionUsecases {

    ProductionResponseDto create(ProductionRequestDto request);

    ProductionResponseDto getByIdentifier(String identifier);

    PagedListWithAnalytics<ProductionBasicAnalyticsDto, ProductionListResponseDto> getAll(
            ProductionQueryParams params);

    List<ProductionResponseDto> getByFactoryId(String factoryId);

    ProductionResponseDto update(String identifier, ProductionRequestDto request);
}
