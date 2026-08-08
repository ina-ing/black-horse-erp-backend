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
import com.inaing.blackhorse_erp.module.role.domain.Role;
import com.inaing.blackhorse_erp.module.warehouse.domain.Warehouse;
import com.inaing.blackhorse_erp.module.warehouse.service.IWarehouseService;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;
import com.inaing.blackhorse_erp.security.context.CurrentUserProvider;
import com.inaing.blackhorse_erp.utils.ItemsUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateProductionOrderUsecase {

    private final CurrentUserProvider currentUserProvider;
    private final IWarehouseService warehouseService;
    private final IProductionOrderService productionOrderService;
    private final IProductVariantSizeService productVariantSizeService;
    private final ProductionOrderMapper productionOrderMapper;

    @Transactional
    public ProductionOrderResponseDto execute(ProductionOrderCreationRequestDto request) {

        AuthPrincipal principal = currentUserProvider.currentPrincipal()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        Warehouse warehouse = warehouseService.getByIdentifier(request.warehouse());
        if (warehouse == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "Warehouse not found " + request.warehouse());
        }

        if (!principal.role().equals(Role.ADMIN.toString())
                && !principal.id().equals(warehouse.getManager().getId())) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

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
        return productionOrderMapper.toResponse(productionOrderService.create(order));
    }
}
