package com.inaing.blackhorse_erp.module.supply.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.factory.domain.Factory;
import com.inaing.blackhorse_erp.module.factory.service.IFactoryService;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariantSize;
import com.inaing.blackhorse_erp.module.product.service.IProductVariantSizeService;
import com.inaing.blackhorse_erp.module.supply.domain.Supply;
import com.inaing.blackhorse_erp.module.supply.domain.SupplyItem;
import com.inaing.blackhorse_erp.module.supply.dto.request.SupplyCreationRequestDto;
import com.inaing.blackhorse_erp.module.supply.dto.response.SupplyResponseDto;
import com.inaing.blackhorse_erp.module.supply.mapper.SupplyMapper;
import com.inaing.blackhorse_erp.module.supply.service.ISupplyService;
import com.inaing.blackhorse_erp.module.warehouse.domain.Warehouse;
import com.inaing.blackhorse_erp.module.warehouse.service.IWarehouseService;
import com.inaing.blackhorse_erp.utils.ItemsUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateSupplyUsecase {

    private final SupplyMapper supplyMapper;
    private final ISupplyService supplyService;
    private final IFactoryService factoryService;
    private final IWarehouseService warehouseService;
    private final IProductVariantSizeService productVariantSizeService;

    @Transactional
    public SupplyResponseDto execute(SupplyCreationRequestDto request) {

        Warehouse warehouse = warehouseService.getByIdentifier(request.suppliedTo());
        if (warehouse == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "Warehouse not found " + request.suppliedTo());
        }
        Factory factory = factoryService.getByIdentifier(request.suppliedBy());
        if (factory == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "Factory not found " + request.suppliedBy());
        }

        Supply supply = Supply.builder()
                .suppliedTo(warehouse)
                .suppliedBy(factory)
                .build();
        ItemsUtils.mergeItems(request.items()).forEach((variantSizeId, quantity) -> {
            ProductVariantSize variantSize = productVariantSizeService.getById(variantSizeId);
            SupplyItem item = SupplyItem.builder()
                    .variantSize(variantSize)
                    .quantity(quantity)
                    .build();
            supply.addItem(item);
        });
        supply.recalculateTotals();
        return supplyMapper.toResponse(supplyService.create(supply));
    }
}
