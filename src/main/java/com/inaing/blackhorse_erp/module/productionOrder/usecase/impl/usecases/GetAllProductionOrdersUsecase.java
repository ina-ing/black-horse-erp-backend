package com.inaing.blackhorse_erp.module.productionOrder.usecase.impl.usecases;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.module.productionOrder.dto.response.ProductionOrderResponseDto;
import com.inaing.blackhorse_erp.module.productionOrder.mapper.ProductionOrderMapper;
import com.inaing.blackhorse_erp.module.productionOrder.service.IProductionOrderService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetAllProductionOrdersUsecase {

    private final ProductionOrderMapper productionOrderMapper;
    private final IProductionOrderService productionOrderService;

    @Transactional(readOnly = true)
    public List<ProductionOrderResponseDto> execute() {
        return productionOrderService.getAll()
                .stream()
                .map(productionOrderMapper::toResponse)
                .toList();
    }
}
