package com.inaing.blackhorse_erp.module.production.usecase.impl.usecases;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.module.production.dto.response.ProductionResponseDto;
import com.inaing.blackhorse_erp.module.production.mapper.ProductionMapper;
import com.inaing.blackhorse_erp.module.production.service.IProductionService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetFactoryProductionUsecase {

    private final ProductionMapper productionMapper;
    private final IProductionService productionService;

    @Transactional(readOnly = true)
    public List<ProductionResponseDto> execute(String factoryId) {
        return productionService.getByFactoryId(factoryId)
                .stream()
                .map(productionMapper::toResponse)
                .toList();
    }
}
