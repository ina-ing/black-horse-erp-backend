package com.inaing.blackhorse_erp.module.returns.usecase.impl.usecases;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.exception.exceptions.BusinessRuleException;
import com.inaing.blackhorse_erp.module.inventory.domain.enums.LocationType;
import com.inaing.blackhorse_erp.module.inventory.service.IInventoryService;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariantSize;
import com.inaing.blackhorse_erp.module.returns.domain.Return;
import com.inaing.blackhorse_erp.module.returns.domain.ReturnItem;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnReason;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnStatus;
import com.inaing.blackhorse_erp.module.returns.dto.response.ReturnResponseDto;
import com.inaing.blackhorse_erp.module.returns.mapper.ReturnMapper;
import com.inaing.blackhorse_erp.module.returns.service.IReturnService;
import com.inaing.blackhorse_erp.module.returns.service.IReturnStatusHistoryService;
import com.inaing.blackhorse_erp.module.warehouse.domain.Warehouse;
import com.inaing.blackhorse_erp.module.warehouse.service.IWarehouseService;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;
import com.inaing.blackhorse_erp.security.context.CurrentUserProvider;

import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityAction;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityEntityType;
import com.inaing.blackhorse_erp.module.activityLog.service.IActivityLogService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AcceptStockReturnUsecase {

    private final ReturnMapper returnMapper;
    private final IReturnService returnService;
    private final IInventoryService inventoryService;
    private final CurrentUserProvider currentUserProvider;
    private final IWarehouseService warehouseService;
    private final IReturnStatusHistoryService returnStatusHistoryService;
    private final IActivityLogService activityLogService;

    @Transactional
    public ReturnResponseDto execute(String id) {

        Return ret = returnService.getByIdentifier(id);
        if (ret == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "Return not found" + id);
        }

        if (ret.getStatus() != ReturnStatus.APPROVED || ret.getReason() != ReturnReason.STOCK) {
            throw new BusinessRuleException(
                    "RETURN_NOT_ACCEPTABLE",
                    "Only approved stock returns can be accepted.");
        }

        AuthPrincipal principal = currentUserProvider.currentPrincipal()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        ret.setStatus(ReturnStatus.COMPLETED);
        returnStatusHistoryService.record(ret, ReturnStatus.COMPLETED, ActionTrigger.MANUAL, principal);

        Warehouse warehouse = warehouseService.getWarehouse();
        if (warehouse == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "No warehouse found");
        }
        Map<ProductVariantSize, Integer> quantities = ret.getItems().stream()
                .collect(Collectors.toMap(ReturnItem::getVariantSize, ReturnItem::getQuantity));

        inventoryService.transfer(
                LocationType.RETAILER, ret.getRetailer().getId(),
                LocationType.WAREHOUSE, warehouse.getId(),
                quantities);
        Return saved = returnService.update(ret);

        activityLogService.record(
                ActivityAction.RETURN_STOCK_ACCEPTED,
                "Accepted returned stock from return " + saved.getCode() + " into inventory.",
                ActivityEntityType.RETURN, saved.getId(), saved.getCode(),
                ActionTrigger.MANUAL);

        return returnMapper.toResponse(saved);
    }
}

