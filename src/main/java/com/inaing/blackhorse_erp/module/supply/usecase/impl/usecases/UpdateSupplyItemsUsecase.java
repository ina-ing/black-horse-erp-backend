package com.inaing.blackhorse_erp.module.supply.usecase.impl.usecases;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.exception.exceptions.BusinessRuleException;
import com.inaing.blackhorse_erp.module.inventory.domain.enums.LocationType;
import com.inaing.blackhorse_erp.module.inventory.service.IInventoryService;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariantSize;
import com.inaing.blackhorse_erp.module.product.service.IProductVariantSizeService;
import com.inaing.blackhorse_erp.module.supply.domain.Supply;
import com.inaing.blackhorse_erp.module.supply.domain.SupplyItem;
import com.inaing.blackhorse_erp.module.supply.domain.enums.SupplyStatus;
import com.inaing.blackhorse_erp.module.supply.dto.request.SupplyItemsUpdateRequestDto;
import com.inaing.blackhorse_erp.module.supply.dto.response.SupplyResponseDto;
import com.inaing.blackhorse_erp.module.supply.mapper.SupplyMapper;
import com.inaing.blackhorse_erp.module.supply.service.ISupplyService;
import com.inaing.blackhorse_erp.utils.ItemsUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateSupplyItemsUsecase {

    private final SupplyMapper supplyMapper;
    private final ISupplyService supplyService;
    private final IProductVariantSizeService productVariantSizeService;
    private final IInventoryService inventoryService;

    @Transactional
    public SupplyResponseDto execute(String identifier, SupplyItemsUpdateRequestDto request) {

        Supply supply = supplyService.getByIdentifier(identifier);
        if (supply == null) {
            throw new AppException(ErrorCode.SUPPLY_NOT_FOUND);
        }

        if (supply.getStatus() != SupplyStatus.ISSUE) {
            throw new BusinessRuleException(
                    "SUPPLY_NOT_EDITABLE",
                    "Only supplies flagged as issue can have their items updated.");
        }

        Map<String, SupplyItem> existingItems = supply.getItems()
                .stream()
                .collect(Collectors.toMap(item -> item.getVariantSize().getId(), Function.identity()));

        Map<ProductVariantSize, Integer> increases = new HashMap<>();
        Map<ProductVariantSize, Integer> decreases = new HashMap<>();

        ItemsUtils.mergeItems(request.items()).forEach((variantSizeId, quantity) -> {
            SupplyItem item = existingItems.remove(variantSizeId);
            int previousQuantity;

            if (item == null) {
                ProductVariantSize variantSize = productVariantSizeService.getById(variantSizeId);
                item = SupplyItem.builder()
                        .variantSize(variantSize)
                        .build();
                supply.addItem(item);
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
            supply.removeItem(item);
        });

        String factoryId = supply.getSuppliedBy().getId();
        if (!decreases.isEmpty()) {
            inventoryService.credit(LocationType.FACTORY, factoryId, decreases);
        }
        if (!increases.isEmpty()) {
            inventoryService.debit(LocationType.FACTORY, factoryId, increases);
        }

        supply.setStatus(SupplyStatus.PENDING);
        supply.recalculateTotals();
        return supplyMapper.toResponse(supplyService.update(supply));
    }
}
