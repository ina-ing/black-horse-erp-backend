package com.inaing.blackhorse_erp.module.productionOrder.usecase;

import java.util.List;

import com.inaing.blackhorse_erp.module.productionOrder.dto.request.ProductionOrderCreationRequstDto;
import com.inaing.blackhorse_erp.module.productionOrder.dto.response.ProductionOrderResponseDto;

public interface IProductionOrderUsecases {

    ProductionOrderResponseDto create(ProductionOrderCreationRequstDto request);

    ProductionOrderResponseDto getByIdentifier(String code);

    List<ProductionOrderResponseDto> getAll();

    ProductionOrderResponseDto accept(String identifier);
}
