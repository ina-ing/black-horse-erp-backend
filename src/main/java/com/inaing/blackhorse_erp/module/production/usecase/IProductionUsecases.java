package com.inaing.blackhorse_erp.module.production.usecase;

import java.util.List;

import com.inaing.blackhorse_erp.module.production.dto.request.ProductionRequestDto;
import com.inaing.blackhorse_erp.module.production.dto.response.ProductionResponseDto;

public interface IProductionUsecases {

    ProductionResponseDto create(ProductionRequestDto request);

    ProductionResponseDto getByIdentifier(String identifier);

    List<ProductionResponseDto> getAll();

    ProductionResponseDto update(String identifier, ProductionRequestDto request);
}
