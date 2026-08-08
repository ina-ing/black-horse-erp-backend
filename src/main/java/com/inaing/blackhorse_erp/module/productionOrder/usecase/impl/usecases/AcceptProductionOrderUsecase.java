package com.inaing.blackhorse_erp.module.productionOrder.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.exception.exceptions.BusinessRuleException;
import com.inaing.blackhorse_erp.module.productionOrder.domain.ProductionOrder;
import com.inaing.blackhorse_erp.module.productionOrder.domain.enums.ProductionOrderStatus;
import com.inaing.blackhorse_erp.module.productionOrder.dto.response.ProductionOrderResponseDto;
import com.inaing.blackhorse_erp.module.productionOrder.mapper.ProductionOrderMapper;
import com.inaing.blackhorse_erp.module.productionOrder.service.IProductionOrderService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AcceptProductionOrderUsecase {

    private final IProductionOrderService productionOrderService;
    private final ProductionOrderMapper productionOrderMapper;

    @Transactional
    public ProductionOrderResponseDto execute(String identifier) {

        ProductionOrder order = productionOrderService.getByIdentifier(identifier);
        if (order == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "Production order not found " + identifier);
        }

        if (order.getStatus() != ProductionOrderStatus.PENDING) {
            throw new BusinessRuleException(
                    "PRODUCTION_ORDER_NOT_ACCEPTABLE",
                    "Only pending production orders can be accepted.");
        }

        order.setStatus(ProductionOrderStatus.ACCEPTED);
        
        // add the items to production backlog

        return productionOrderMapper.toResponse(productionOrderService.update(order));
    }
}
