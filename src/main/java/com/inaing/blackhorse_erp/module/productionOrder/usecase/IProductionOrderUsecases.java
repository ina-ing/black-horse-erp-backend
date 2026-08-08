package com.inaing.blackhorse_erp.module.productionOrder.usecase;

import java.util.List;

import com.inaing.blackhorse_erp.module.productionOrder.dto.request.ProductionOrderCreationRequestDto;
import com.inaing.blackhorse_erp.module.productionOrder.dto.response.ProductionOrderResponseDto;

public interface IProductionOrderUsecases {

    ProductionOrderResponseDto create(ProductionOrderCreationRequestDto request);

    ProductionOrderResponseDto getByIdentifier(String code);

    List<ProductionOrderResponseDto> getAll();

    ProductionOrderResponseDto accept(String identifier);
}
