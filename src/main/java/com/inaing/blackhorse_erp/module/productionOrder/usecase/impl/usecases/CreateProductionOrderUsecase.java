package com.inaing.blackhorse_erp.module.productionOrder.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariantSize;
import com.inaing.blackhorse_erp.module.product.service.IProductVariantSizeService;
import com.inaing.blackhorse_erp.module.productionOrder.domain.ProductionOrder;
import com.inaing.blackhorse_erp.module.productionOrder.domain.ProductionOrderItem;
import com.inaing.blackhorse_erp.module.productionOrder.dto.request.ProductionOrderCreationRequestDto;
import com.inaing.blackhorse_erp.module.productionOrder.dto.response.ProductionOrderResponseDto;
import com.inaing.blackhorse_erp.module.productionOrder.mapper.ProductionOrderMapper;
import com.inaing.blackhorse_erp.module.productionOrder.service.IProductionOrderService;
import com.inaing.blackhorse_erp.module.warehouse.authorization.WarehouseAuthorizationService;
import com.inaing.blackhorse_erp.module.warehouse.domain.Warehouse;
import com.inaing.blackhorse_erp.module.warehouse.service.IWarehouseService;
import com.inaing.blackhorse_erp.utils.ItemsUtils;

import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityAction;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityEntityType;
import com.inaing.blackhorse_erp.module.activityLog.service.IActivityLogService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateProductionOrderUsecase {

    private final IWarehouseService warehouseService;
    private final IProductionOrderService productionOrderService;
    private final IProductVariantSizeService productVariantSizeService;
    private final ProductionOrderMapper productionOrderMapper;
    private final WarehouseAuthorizationService warehouseAuthorizationService;
    private final IActivityLogService activityLogService;

    @Transactional
    public ProductionOrderResponseDto execute(ProductionOrderCreationRequestDto request) {

        Warehouse warehouse = warehouseService.getByIdentifier(request.warehouse());
        if (warehouse == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "Warehouse not found " + request.warehouse());
        }
        warehouseAuthorizationService.assertCanAccess(warehouse);

        ProductionOrder order = ProductionOrder.builder()
                .warehouse(warehouse)
                .build();

        ItemsUtils.mergeItems(request.items()).forEach((variantSizeId, quantity) -> {
            ProductVariantSize variantSize = productVariantSizeService.getById(variantSizeId);
            ProductionOrderItem item = ProductionOrderItem.builder()
                    .variantSize(variantSize)
                    .quantity(quantity)
                    .build();
            order.addItem(item);
        });
        order.recalculateTotals();
        ProductionOrder created = productionOrderService.create(order);

        activityLogService.record(
                ActivityAction.PRODUCTION_ORDER_CREATED,
                "Placed production order " + created.getCode() + " from warehouse "
                        + warehouse.getName() + ".",
                ActivityEntityType.PRODUCTION_ORDER, created.getId(), created.getCode(),
                ActionTrigger.CREATION);

        return productionOrderMapper.toResponse(created);
    }
}
