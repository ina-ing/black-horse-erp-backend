package com.inaing.blackhorse_erp.module.order.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.exception.exceptions.BusinessRuleException;
import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.order.domain.Order;
import com.inaing.blackhorse_erp.module.order.domain.OrderItem;
import com.inaing.blackhorse_erp.module.order.domain.enums.OrderStatus;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderCreationRequestDto;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderResponseDto;
import com.inaing.blackhorse_erp.module.order.mapper.OrderMapper;
import com.inaing.blackhorse_erp.module.order.service.IOrderService;
import com.inaing.blackhorse_erp.module.order.service.IOrderStatusHistoryService;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariantSize;
import com.inaing.blackhorse_erp.module.product.service.IProductVariantSizeService;
import com.inaing.blackhorse_erp.module.retailer.domain.Retailer;
import com.inaing.blackhorse_erp.module.retailer.service.IRetailerService;
import com.inaing.blackhorse_erp.module.role.domain.Role;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;
import com.inaing.blackhorse_erp.security.context.CurrentUserProvider;
import com.inaing.blackhorse_erp.utils.ItemsUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateOrderUsecase {
    private final OrderMapper orderMapper;
    private final IRetailerService retailerService;
    private final IOrderService orderService;
    private final IOrderStatusHistoryService orderStatusHistoryService;
    private final IProductVariantSizeService variantSizeService;
    private final CurrentUserProvider currentUserProvider;

    @Transactional
    public OrderResponseDto execute(OrderCreationRequestDto request) {

        AuthPrincipal principal = currentUserProvider.currentPrincipal()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        Role creatorRole = Role.fromName(principal.role());

        Retailer retailer = retailerService.getByIdentifier(request.retailer());
        if (retailer == null) {
            throw new AppException(ErrorCode.RETAILER_NOT_FOUND, "Retailer Not Found " + request.retailer());
        }

        Employee salesman = retailer.getAssignedSalesman();
        if (salesman == null) {
            throw new BusinessRuleException("ASSIGNED_SALESMAN_NOT_FOUND",
                    "The retailer has no assigned salesman: " + retailer.getCode());
        }

        OrderStatus status = (creatorRole == Role.SALES || creatorRole == Role.ADMIN)
                ? OrderStatus.APPROVED
                : OrderStatus.PENDING;

        Order order = Order.builder()
                .retailer(retailer)
                .handledBy(salesman)
                .note(request.note())
                .status(status)
                .createdByRole(creatorRole)
                .build();

        ItemsUtils.mergeItems(request.items()).forEach((variantSizeId, quantity) -> {
            ProductVariantSize variantSize = variantSizeService.getById(variantSizeId);
            OrderItem item = OrderItem.builder()
                    .variantSize(variantSize)
                    .quantity(quantity)
                    .requestedQuantity(quantity)
                    .build();
            order.addItem(item);
        });
        order.recalculateTotals();

        Order created = orderService.create(order);
        orderStatusHistoryService.record(created, status, ActionTrigger.CREATION, principal);

        return orderMapper.toResponse(created);

    }
}
