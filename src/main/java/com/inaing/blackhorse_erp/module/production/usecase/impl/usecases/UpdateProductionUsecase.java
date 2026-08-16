package com.inaing.blackhorse_erp.module.production.usecase.impl.usecases;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.factory.domain.Factory;
import com.inaing.blackhorse_erp.module.inventory.domain.enums.LocationType;
import com.inaing.blackhorse_erp.module.inventory.service.IInventoryService;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariantSize;
import com.inaing.blackhorse_erp.module.product.service.IProductVariantSizeService;
import com.inaing.blackhorse_erp.module.production.domain.Production;
import com.inaing.blackhorse_erp.module.production.domain.ProductionItem;
import com.inaing.blackhorse_erp.module.production.dto.request.ProductionRequestDto;
import com.inaing.blackhorse_erp.module.production.dto.response.ProductionResponseDto;
import com.inaing.blackhorse_erp.module.production.mapper.ProductionMapper;
import com.inaing.blackhorse_erp.module.production.service.IProductionService;
import com.inaing.blackhorse_erp.utils.ItemsUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateProductionUsecase {

    private final ProductionMapper productionMapper;
    private final IProductionService productionService;
    private final IInventoryService inventoryService;
    private final IProductVariantSizeService productVariantSizeService;

    @Transactional
    public ProductionResponseDto execute(String identifier, ProductionRequestDto request) {

        Production production = productionService.getByIdentifier(identifier);
        if (production == null) {
            throw new AppException(ErrorCode.PRODUCTION_NOT_FOUND, "Production not found " + identifier);
        }

        Factory factory = production.getFactory();
        if (!factory.getId().equals(request.factory()) && !factory.getCode().equals(request.factory())) {
            throw new AppException(ErrorCode.BUSINESS_RULE_VIOLATION,
                    "Production factory cannot be changed after creation");
        }

        Map<String, ProductionItem> existingItems = production.getItems()
                .stream()
                .collect(Collectors.toMap(item -> item.getVariantSize().getId(), Function.identity()));

        Map<ProductVariantSize, Integer> increases = new HashMap<>();
        Map<ProductVariantSize, Integer> decreases = new HashMap<>();

        ItemsUtils.mergeItems(request.items()).forEach((variantSizeId, quantity) -> {
            ProductionItem item = existingItems.remove(variantSizeId);
            int previousQuantity;

            if (item == null) {
                ProductVariantSize variantSize = productVariantSizeService.getById(variantSizeId);
                item = ProductionItem.builder()
                        .variantSize(variantSize)
                        .build();
                production.addItem(item);
                previousQuantity = 0;
            } else {
                previousQuantity = item.getQuantity();
            }
            item.setQuantity(quantity);

            int delta = quantity - previousQuantity;
            if (delta > 0) {
                increases.merge(item.getVariantSize(), delta, Integer::sum);
            } else if (delta < 0) {
                decreases.merge(item.getVariantSize(), -delta, Integer::sum);
            }
        });

        existingItems.values().forEach(item -> {
            decreases.merge(item.getVariantSize(), item.getQuantity(), Integer::sum);
            production.removeItem(item);
        });

        if (!increases.isEmpty()) {
            inventoryService.credit(LocationType.FACTORY, factory.getId(), increases);
        }
        if (!decreases.isEmpty()) {
            inventoryService.debit(LocationType.FACTORY, factory.getId(), decreases);
        }
        production.recalculateTotals();

        return productionMapper.toResponse(productionService.update(production));
    }
}
