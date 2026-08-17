package com.inaing.blackhorse_erp.module.supply.usecase.impl.usecases;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.exception.exceptions.BusinessRuleException;
import com.inaing.blackhorse_erp.module.inventory.domain.enums.LocationType;
import com.inaing.blackhorse_erp.module.inventory.service.IInventoryService;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariantSize;
import com.inaing.blackhorse_erp.module.supply.domain.Supply;
import com.inaing.blackhorse_erp.module.supply.domain.SupplyItem;
import com.inaing.blackhorse_erp.module.supply.domain.enums.SupplyStatus;
import com.inaing.blackhorse_erp.module.supply.dto.request.SupplyStatusUpdateRequestDto;
import com.inaing.blackhorse_erp.module.supply.dto.response.SupplyResponseDto;
import com.inaing.blackhorse_erp.module.supply.mapper.SupplyMapper;
import com.inaing.blackhorse_erp.module.supply.service.ISupplyService;

import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityAction;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityEntityType;
import com.inaing.blackhorse_erp.module.activityLog.service.IActivityLogService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateSupplyStatusUsecase {

    private final SupplyMapper supplyMapper;
    private final ISupplyService supplyService;
    private final IInventoryService inventoryService;
    private final IActivityLogService activityLogService;

    @Transactional
    public SupplyResponseDto execute(String identifier, SupplyStatusUpdateRequestDto request) {

        Supply supply = supplyService.getByIdentifier(identifier);
        if (supply == null) {
            throw new AppException(ErrorCode.SUPPLY_NOT_FOUND);
        }

        if (supply.getStatus() != SupplyStatus.PENDING) {
            throw new BusinessRuleException(
                    "SUPPLY_NOT_UPDATABLE",
                    "Only pending supplies can be accepted or flagged as issue.");
        }

        switch (request.status()) {
            case ACCEPTED -> accept(supply);
            case ISSUE -> issue(supply);
            default -> throw new BusinessRuleException(
                    "INVALID_STATUS_TRANSITION",
                    "Supply status can only be updated to accepted or issue.");
        }

        Supply saved = supplyService.update(supply);

        activityLogService.record(
                saved.getStatus() == SupplyStatus.ISSUE
                        ? ActivityAction.SUPPLY_DISPUTED
                        : ActivityAction.SUPPLY_ACCEPTED,
                saved.getStatus() == SupplyStatus.ISSUE
                        ? "Flagged an issue on supply " + saved.getCode() + "."
                        : "Accepted supply " + saved.getCode() + " into warehouse "
                                + saved.getSuppliedTo().getName() + ".",
                ActivityEntityType.SUPPLY, saved.getId(), saved.getCode(),
                ActionTrigger.MANUAL);

        return supplyMapper.toResponse(saved);
    }

    private void accept(Supply supply) {
        supply.setStatus(SupplyStatus.ACCEPTED);

        Map<ProductVariantSize, Integer> quantities = supply.getItems().stream()
                .collect(Collectors.toMap(SupplyItem::getVariantSize, SupplyItem::getQuantity));
        inventoryService.transfer(LocationType.FACTORY, supply.getSuppliedBy().getId(), LocationType.WAREHOUSE,
                supply.getSuppliedTo().getId(), quantities);
    }

    private void issue(Supply supply) {
        supply.setStatus(SupplyStatus.ISSUE);
        // TODO: notify admin once notification module is available.
    }
}