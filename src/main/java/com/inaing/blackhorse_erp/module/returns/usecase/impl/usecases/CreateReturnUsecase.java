package com.inaing.blackhorse_erp.module.returns.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.exception.exceptions.BusinessRuleException;
import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariantSize;
import com.inaing.blackhorse_erp.module.product.service.IProductVariantSizeService;
import com.inaing.blackhorse_erp.module.retailer.domain.Retailer;
import com.inaing.blackhorse_erp.module.retailer.service.IRetailerService;
import com.inaing.blackhorse_erp.module.returns.domain.Return;
import com.inaing.blackhorse_erp.module.returns.domain.ReturnItem;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnReason;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnStatus;
import com.inaing.blackhorse_erp.module.returns.dto.request.ReturnCreationRequestDto;
import com.inaing.blackhorse_erp.module.returns.dto.response.ReturnResponseDto;
import com.inaing.blackhorse_erp.module.returns.mapper.ReturnMapper;
import com.inaing.blackhorse_erp.module.returns.service.IReturnService;
import com.inaing.blackhorse_erp.module.returns.service.IReturnStatusHistoryService;
import com.inaing.blackhorse_erp.module.role.domain.Role;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;
import com.inaing.blackhorse_erp.security.context.CurrentUserProvider;
import com.inaing.blackhorse_erp.utils.ItemsUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateReturnUsecase {

    private final ReturnMapper returnMapper;
    private final IReturnService returnService;
    private final IRetailerService retailerService;
    private final CurrentUserProvider currentUserProvider;
    private final IProductVariantSizeService variantSizeService;
    private final IReturnStatusHistoryService returnStatusHistoryService;

    @Transactional
    public ReturnResponseDto execute(ReturnCreationRequestDto request) {

        AuthPrincipal principal = currentUserProvider.currentPrincipal()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        Role createrRole = Role.fromName(principal.role());

        Retailer retailer = retailerService.getByIdentifier(request.retailer());
        if (retailer == null) {
            throw new AppException(ErrorCode.RETAILER_NOT_FOUND, "Retailer Not Found " + request.retailer());
        }

        Employee salesman = retailer.getAssignedSalesman();
        if (salesman == null) {
            throw new BusinessRuleException("ASSIGNED_SALESMAN_NOT_FOUND",
                    "The retailer has no assigned salesman: " + retailer.getCode());
        }

        ReturnStatus status = (createrRole == Role.SALES || createrRole == Role.ADMIN)
                ? ReturnStatus.APPROVED
                : ReturnStatus.PENDING;

        Return ret = Return.builder()
                .retailer(retailer)
                .handledBy(salesman)
                .reason(ReturnReason.fromName(request.reason()))
                .status(status)
                .createdByRole(createrRole)
                .note(request.note())
                .build();

        ItemsUtils.mergeItems(request.items()).forEach((variantSizeId, quantity) -> {
            ProductVariantSize variantSize = variantSizeService.getById(variantSizeId);
            ReturnItem item = ReturnItem.builder()
                    .variantSize(variantSize)
                    .quantity(quantity)
                    .build();
            ret.addItem(item);
        });
        ret.recalculateTotals();

        Return created = returnService.create(ret);
        returnStatusHistoryService.record(created, status, ActionTrigger.CREATION, principal);

        return returnMapper.toResponse(created);
    }
}
