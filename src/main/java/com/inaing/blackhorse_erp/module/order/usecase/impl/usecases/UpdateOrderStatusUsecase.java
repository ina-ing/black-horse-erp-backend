package com.inaing.blackhorse_erp.module.order.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.exception.exceptions.BusinessRuleException;
import com.inaing.blackhorse_erp.module.order.domain.Order;
import com.inaing.blackhorse_erp.module.order.domain.enums.OrderStatus;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderStatusUpdateRequestDto;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderResponseDto;
import com.inaing.blackhorse_erp.module.order.mapper.OrderMapper;
import com.inaing.blackhorse_erp.module.order.service.IOrderService;
import com.inaing.blackhorse_erp.module.order.service.IOrderStatusHistoryService;
import com.inaing.blackhorse_erp.module.role.domain.Role;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;
import com.inaing.blackhorse_erp.security.context.CurrentUserProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateOrderStatusUsecase {

    private final IOrderService orderService;
    private final IOrderStatusHistoryService orderStatusHistoryService;
    private final CurrentUserProvider currentUserProvider;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponseDto execute(String id, OrderStatusUpdateRequestDto request) {

        Order order = orderService.getByIdentifier(id);
        if (order == null) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }

        AuthPrincipal principal = currentUserProvider.currentPrincipal()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        Role role = Role.fromName(principal.role());

        switch (request.status()) {
            case APPROVED -> approve(order, role, principal);
            case PROCESSING -> startProcessing(role, order, principal);
            case CANCELLED -> cancel(order, role, principal);
            default -> throw new BusinessRuleException(
                    "INVALID_STATUS_TRANSITION",
                    "This status cannot be set manually.");
        }
        return orderMapper.toResponse(orderService.update(order));
    }

    private void approve(Order order, Role role, AuthPrincipal principal) {

        if (role != Role.ADMIN) {
            if (role != Role.SALES
                    || order.getHandledBy() == null
                    || !principal.id().equals(order.getHandledBy().getId())) {

                throw new BusinessRuleException(
                        "ORDER_APPROVE_DENIED",
                        "You are not allowed to approve this order.");
            }
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessRuleException(
                    "ORDER_NOT_APPROVABLE",
                    "Only pending orders can be approved.");
        }

        boolean hasActiveItems = order.getItems()
                .stream()
                .anyMatch(item -> item.getQuantity() > 0);

        if (!hasActiveItems) {
            throw new BusinessRuleException(
                    "EMPTY_ORDER",
                    "Cannot approve an order with no active items.");
        }

        order.setStatus(OrderStatus.APPROVED);

        orderStatusHistoryService.record(order, OrderStatus.APPROVED, ActionTrigger.MANUAL,
                principal);
    }

    private void startProcessing(Role role, Order order, AuthPrincipal principal) {

        if (role != Role.WAREHOUSE) {
            throw new BusinessRuleException(
                    "ORDER_ACCEPT_DENIED",
                    "Only warehouse users can accept orders.");
        }

        if (order.getStatus() != OrderStatus.APPROVED) {
            throw new BusinessRuleException(
                    "ORDER_NOT_ACCEPTABLE",
                    "Only approved orders can be accepted by the warehouse.");
        }

        order.setStatus(OrderStatus.PROCESSING);

        orderStatusHistoryService.record(order, OrderStatus.PROCESSING,
                ActionTrigger.MANUAL, principal);
    }

    private void cancel(Order order, Role role, AuthPrincipal principal) {

        if (role != Role.ADMIN) {
            switch (role) {
                case RETAILER -> {
                    if (!order.getRetailer().getId().equals(principal.id())) {
                        throw new BusinessRuleException(
                                "ORDER_CANCEL_DENIED",
                                "You can only cancel your own orders.");
                    }
                }

                case SALES -> {
                    if (order.getHandledBy() == null
                            || !order.getHandledBy().getId().equals(principal.id())) {
                        throw new BusinessRuleException(
                                "ORDER_CANCEL_DENIED",
                                "You are not allowed to cancel this order.");
                    }
                }
                default -> throw new BusinessRuleException(
                        "ORDER_CANCEL_DENIED",
                        "You are not allowed to cancel orders.");
            }
        }

        if (order.getStatus() != OrderStatus.PENDING
                && order.getStatus() != OrderStatus.APPROVED) {
            throw new BusinessRuleException(
                    "ORDER_NOT_CANCELLABLE",
                    "Only pending or approved orders can be cancelled.");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderStatusHistoryService.record(order, OrderStatus.CANCELLED, ActionTrigger.CANCELLATION, principal);
    }
}
