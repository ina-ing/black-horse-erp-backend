package com.inaing.blackhorse_erp.module.productionOrder.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.productionOrder.domain.ProductionOrder;
import com.inaing.blackhorse_erp.module.productionOrder.dto.response.ProductionOrderResponseDto;
import com.inaing.blackhorse_erp.module.productionOrder.mapper.ProductionOrderMapper;
import com.inaing.blackhorse_erp.module.productionOrder.service.IProductionOrderService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetProductionOrderUsecase {

    private final ProductionOrderMapper productionOrderMapper;
    private final IProductionOrderService productionOrderService;

    @Transactional(readOnly = true)
    public ProductionOrderResponseDto execute(String code) {

        ProductionOrder order = productionOrderService.getByIdentifier(code);
        if (order == null) {
            throw new AppException(ErrorCode.PRODUCTION_ORDER_NOT_FOUND, "Order not found " + code);
        }

        return productionOrderMapper.toResponse(order);
    }
}
