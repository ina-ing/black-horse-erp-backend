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
public class GetAllProductionUsecase {

    private final ProductionMapper productionMapper;
    private final IProductionService productionService;

    @Transactional(readOnly = true)
    public List<ProductionResponseDto> execute() {
        return productionService.getAll()
                .stream()
                .map(productionMapper::toResponse)
                .toList();
    }
}
