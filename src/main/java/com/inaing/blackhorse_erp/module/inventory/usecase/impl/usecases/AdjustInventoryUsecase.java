package com.inaing.blackhorse_erp.module.inventory.usecase.impl.usecases;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.module.inventory.domain.Inventory;
import com.inaing.blackhorse_erp.module.inventory.dto.request.InventoryAdjustmentItemRequestDto;
import com.inaing.blackhorse_erp.module.inventory.dto.request.InventoryAdjustmentRequestDto;
import com.inaing.blackhorse_erp.module.inventory.dto.response.InventoryResponseDto;
import com.inaing.blackhorse_erp.module.inventory.mapper.InventoryMapper;
import com.inaing.blackhorse_erp.module.inventory.service.IInventoryService;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariantSize;
import com.inaing.blackhorse_erp.module.product.service.IProductVariantSizeService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdjustInventoryUsecase {

    private final IInventoryService inventoryService;
    private final IProductVariantSizeService productVariantSizeService;
    private final InventoryMapper inventoryMapper;

    @Transactional
    public InventoryResponseDto execute(InventoryAdjustmentRequestDto request) {

        Map<ProductVariantSize, Integer> quantities = new LinkedHashMap<>();
        for (InventoryAdjustmentItemRequestDto item : request.items()) {
            ProductVariantSize variantSize = productVariantSizeService.getById(item.variantSizeId());
            quantities.put(variantSize, item.quantity());
        }

        Inventory inventory = inventoryService.adjust(request.locationType(), request.referenceId(), quantities);
        return inventoryMapper.toResponse(inventory);
    }
}
