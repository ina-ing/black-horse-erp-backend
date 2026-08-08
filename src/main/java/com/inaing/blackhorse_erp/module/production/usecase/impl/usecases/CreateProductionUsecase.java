package com.inaing.blackhorse_erp.module.production.usecase.impl.usecases;

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
public class CreateProductionUsecase {

    private final ProductionMapper productionMapper;
    private final IFactoryService factoryService;
    private final IProductionService productionService;
    private final IProductVariantSizeService productVariantSizeService;

    @Transactional
    public ProductionResponseDto execute(ProductionRequestDto request) {

        Factory factory = factoryService.getByIdentifier(request.factory());
        if (factory == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "Factory not found " + request.factory());
        }

        Production production = Production.builder()
                .factory(factory)
                .build();

        ItemsUtils.mergeItems(request.items()).forEach((variantSizeId, quantity) -> {
            ProductVariantSize variantSize = productVariantSizeService.getById(variantSizeId);
            ProductionItem item = ProductionItem.builder()
                    .variantSize(variantSize)
                    .quantity(quantity)
                    .build();
            production.addItem(item);
        });
        production.recalculateTotals();
        return productionMapper.toResponse(productionService.create(production));
    }
}
