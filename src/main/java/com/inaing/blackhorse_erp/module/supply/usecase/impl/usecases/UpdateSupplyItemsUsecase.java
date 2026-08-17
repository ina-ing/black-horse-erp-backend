package com.inaing.blackhorse_erp.module.supply.usecase.impl.usecases;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.exception.exceptions.BusinessRuleException;
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

import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityAction;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityEntityType;
import com.inaing.blackhorse_erp.module.activityLog.service.IActivityLogService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateSupplyItemsUsecase {

    private final SupplyMapper supplyMapper;
    private final ISupplyService supplyService;
    private final IProductVariantSizeService productVariantSizeService;
    private final IActivityLogService activityLogService;

    @Transactional
    public SupplyResponseDto execute(String identifier, SupplyItemsUpdateRequestDto request) {

        Supply supply = supplyService.getByIdentifier(identifier);
        if (supply == null) {
            throw new AppException(ErrorCode.SUPPLY_NOT_FOUND);
        }

        if (supply.getStatus() != SupplyStatus.ISSUE) {
            throw new BusinessRuleException(
                    "SUPPLY_NOT_EDITABLE",
                    "Only pending supplies can have their items updated.");
        }

        Map<String, SupplyItem> existingItems = supply.getItems()
                .stream()
                .collect(Collectors.toMap(item -> item.getVariantSize().getId(), Function.identity()));

        ItemsUtils.mergeItems(request.items()).forEach((variantSizeId, quantity) -> {
            SupplyItem item = existingItems.remove(variantSizeId);

            if (item == null) {
                ProductVariantSize variantSize = productVariantSizeService.getById(variantSizeId);
                item = SupplyItem.builder()
                        .variantSize(variantSize)
                        .build();
                supply.addItem(item);
            }
            item.setQuantity(quantity);
        });

        existingItems.values().forEach(supply::removeItem);

        supply.setStatus(SupplyStatus.PENDING);
        supply.recalculateTotals();
        Supply updated = supplyService.update(supply);

        activityLogService.record(
                ActivityAction.SUPPLY_UPDATED,
                "Updated items on supply " + updated.getCode() + ".",
                ActivityEntityType.SUPPLY, updated.getId(), updated.getCode(),
                ActionTrigger.MANUAL);

        return supplyMapper.toResponse(updated);
    }
}