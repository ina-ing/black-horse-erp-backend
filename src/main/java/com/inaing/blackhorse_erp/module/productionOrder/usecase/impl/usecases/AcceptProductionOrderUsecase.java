package com.inaing.blackhorse_erp.module.productionOrder.usecase.impl.usecases;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.exception.exceptions.BusinessRuleException;
import com.inaing.blackhorse_erp.module.backlog.service.IBacklogService;
import com.inaing.blackhorse_erp.module.factory.domain.Factory;
import com.inaing.blackhorse_erp.module.factory.service.IFactoryService;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariantSize;
import com.inaing.blackhorse_erp.module.productionOrder.domain.ProductionOrder;
import com.inaing.blackhorse_erp.module.productionOrder.domain.enums.ProductionOrderStatus;
import com.inaing.blackhorse_erp.module.productionOrder.dto.response.ProductionOrderResponseDto;
import com.inaing.blackhorse_erp.module.productionOrder.mapper.ProductionOrderMapper;
import com.inaing.blackhorse_erp.module.productionOrder.service.IProductionOrderService;

import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityAction;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityEntityType;
import com.inaing.blackhorse_erp.module.activityLog.service.IActivityLogService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AcceptProductionOrderUsecase {

    private final IProductionOrderService productionOrderService;
    private final IFactoryService factoryService;
    private final IBacklogService productionBacklogService;
    private final ProductionOrderMapper productionOrderMapper;
    private final IActivityLogService activityLogService;

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

        Factory factory = factoryService.getFactory();

        order.setStatus(ProductionOrderStatus.ACCEPTED);

        Map<ProductVariantSize, Integer> quantities = order.getItems()
                .stream()
                .collect(Collectors.toMap(
                        item -> item.getVariantSize(),
                        item -> item.getQuantity(),
                        Integer::sum));
        productionBacklogService.addQuantities(factory, quantities);

        ProductionOrder accepted = productionOrderService.update(order);

        activityLogService.record(
                ActivityAction.PRODUCTION_ORDER_ACCEPTED,
                "Accepted production order " + accepted.getCode() + " at factory "
                        + factory.getName() + ".",
                ActivityEntityType.PRODUCTION_ORDER, accepted.getId(), accepted.getCode(),
                ActionTrigger.MANUAL);

        return productionOrderMapper.toResponse(accepted);
    }
}