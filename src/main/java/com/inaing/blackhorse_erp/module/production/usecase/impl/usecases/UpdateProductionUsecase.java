package com.inaing.blackhorse_erp.module.production.usecase.impl.usecases;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.factory.domain.Factory;
import com.inaing.blackhorse_erp.module.factory.service.IFactoryService;
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
    private final IFactoryService factoryService;
    private final IProductionService productionService;
    private final IProductVariantSizeService productVariantSizeService;

    @Transactional
    public ProductionResponseDto execute(String identifier, ProductionRequestDto request) {

        Production production = productionService.getByIdentifier(identifier);
        if (production == null) {
            throw new AppException(ErrorCode.PRODUCTION_NOT_FOUND, "Production not found " + identifier);
        }

        Factory factory = factoryService.getByIdentifier(request.factory());
        if (factory == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "Factory not found " + request.factory());
        }
        production.setFactory(factory);

        Map<String, ProductionItem> existingItems = production.getItems()
                .stream()
                .collect(Collectors.toMap(item -> item.getVariantSize().getId(), Function.identity()));

        ItemsUtils.mergeItems(request.items()).forEach((variantSizeId, quantity) -> {
            ProductionItem item = existingItems.remove(variantSizeId);
            if (item == null) {
                ProductVariantSize variantSize = productVariantSizeService.getById(variantSizeId);
                item = ProductionItem.builder()
                        .variantSize(variantSize)
                        .build();
                production.addItem(item);
            }
            item.setQuantity(quantity);
        });

        existingItems.values().forEach(production::removeItem);

        production.recalculateTotals();

        // also update the factory inventory
        return productionMapper.toResponse(productionService.update(production));
    }
}
