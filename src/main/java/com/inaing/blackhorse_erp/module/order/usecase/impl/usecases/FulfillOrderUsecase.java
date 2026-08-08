package com.inaing.blackhorse_erp.module.order.usecase.impl.usecases;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.exception.exceptions.BusinessRuleException;
import com.inaing.blackhorse_erp.module.order.domain.Order;
import com.inaing.blackhorse_erp.module.order.domain.OrderItem;
import com.inaing.blackhorse_erp.module.order.domain.enums.OrderStatus;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderFulfillmentRequestDto;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderItemRequestDto;
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
public class FulfillOrderUsecase {

        private final CurrentUserProvider currentUserProvider;
        private final IOrderService orderService;
        private final IOrderStatusHistoryService orderStatusHistoryService;
        private final OrderMapper orderMapper;

        @Transactional
        public OrderResponseDto execute(String identifier, OrderFulfillmentRequestDto request) {

                Order order = orderService.getByIdentifier(identifier);
                if (order == null) {
                        throw new AppException(ErrorCode.ORDER_NOT_FOUND);
                }

                if (order.getStatus() != OrderStatus.PROCESSING && order.getStatus() != OrderStatus.PARTIAL) {
                        throw new BusinessRuleException(
                                        "ORDER_NOT_FULFILLABLE",
                                        "Only orders in PROCESSING or PARTIAL status can be fulfilled.");
                }

                AuthPrincipal principal = currentUserProvider.currentPrincipal()
                                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
                if (Role.fromName(principal.role()) != Role.WAREHOUSE) {
                        throw new BusinessRuleException(
                                        "ORDER_FULFILL_DENIED",
                                        "You are not allowed to fulfill this order.");
                }

                Map<String, OrderItem> itemsBySku = order.getItems()
                                .stream()
                                .collect(Collectors.toMap(
                                                item -> item.getVariantSize().getId(),
                                                Function.identity()));

                Map<String, Integer> projectedFulfilled = new HashMap<>();

                for (OrderItemRequestDto line : request.items()) {
                        OrderItem item = itemsBySku.get(line.variantSizeId());
                        if (item == null) {
                                throw new BusinessRuleException(
                                                "ITEM_NOT_IN_ORDER",
                                                "Cannot fulfill a SKU that is not on the order: "
                                                                + line.variantSizeId());
                        }

                        int current = projectedFulfilled.getOrDefault(line.variantSizeId(),
                                        item.getFulfilledQuantity());
                        int updated = current + line.quantity();
                        if (updated > item.getQuantity()) {
                                throw new BusinessRuleException(
                                                "OVER_FULFILLMENT",
                                                "Fulfilled quantity exceeds ordered quantity for "
                                                                + item.getVariantSize().getId());
                        }
                        projectedFulfilled.put(line.variantSizeId(), updated);
                }

                projectedFulfilled.forEach(
                                (variantSizeId, fulfilledQuantity) -> itemsBySku.get(variantSizeId)
                                                .setFulfilledQuantity(fulfilledQuantity));

                boolean allFulfilled = order.getItems()
                                .stream()
                                .filter(item -> item.getQuantity() > 0)
                                .allMatch(item -> item.getFulfilledQuantity().equals(item.getQuantity()));

                order.setStatus(allFulfilled ? OrderStatus.COMPLETED : OrderStatus.PARTIAL);

                Order updated = orderService.update(order);
                orderStatusHistoryService.record(updated, updated.getStatus(),
                                ActionTrigger.FULFILLMENT, principal);

                return orderMapper.toResponse(updated);
        }
}
