package com.inaing.blackhorse_erp.module.order.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.exception.exceptions.BusinessRuleException;
import com.inaing.blackhorse_erp.module.order.domain.Order;
import com.inaing.blackhorse_erp.module.order.domain.enums.OrderHistoryTrigger;
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

        AuthPrincipal actor = currentUserProvider.currentPrincipal()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        Role role = Role.fromName(actor.role());

        switch (request.status()) {
            case APPROVED -> approve(order, role, actor);
            case PROCESSING -> startProcessing(role, order, actor);
            case CANCELLED -> cancel(order, role, actor);
            default -> throw new BusinessRuleException(
                    "INVALID_STATUS_TRANSITION",
                    "This status cannot be set manually.");
        }
        return orderMapper.toResponse(orderService.update(order));
    }

    private void approve(Order order, Role role, AuthPrincipal actor) {

        if (role != Role.ADMIN) {
            if (role != Role.SALES
                    || order.getHandledBy() == null
                    || !actor.id().equals(order.getHandledBy().getId())) {

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

        orderStatusHistoryService.record(order, OrderStatus.APPROVED, OrderHistoryTrigger.MANUAL,
                actor);
    }

    private void startProcessing(Role role, Order order, AuthPrincipal actor) {

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
                OrderHistoryTrigger.MANUAL, actor);
    }

    private void cancel(Order order, Role role, AuthPrincipal actor) {

        if (role != Role.RETAILER
                && role != Role.SALES
                && role != Role.ADMIN) {
            throw new BusinessRuleException(
                    "ORDER_CANCEL_DENIED",
                    "You are not allowed to cancel orders.");
        }

        if (order.getStatus() != OrderStatus.PENDING
                && order.getStatus() != OrderStatus.APPROVED) {
            throw new BusinessRuleException(
                    "ORDER_NOT_CANCELLABLE",
                    "Only pending or approved orders can be cancelled.");
        }

        if (role == Role.RETAILER
                && !order.getRetailer().getId().equals(actor.id())) {
            throw new BusinessRuleException(
                    "ORDER_CANCEL_DENIED",
                    "You can only cancel your own orders.");
        }

        if (role == Role.SALES) {
            if (order.getHandledBy() == null
                    || !order.getHandledBy().getId().equals(actor.id())) {
                throw new BusinessRuleException(
                        "ORDER_CANCEL_DENIED",
                        "You are not allowed to cancel this order.");
            }
        }

        order.setStatus(OrderStatus.CANCELLED);

        orderStatusHistoryService.record(order, OrderStatus.CANCELLED,
                OrderHistoryTrigger.CANCELLATION, actor);
    }
}
