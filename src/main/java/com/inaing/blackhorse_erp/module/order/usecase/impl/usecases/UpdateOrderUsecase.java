package com.inaing.blackhorse_erp.module.order.usecase.impl.usecases;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.exception.exceptions.BusinessRuleException;
import com.inaing.blackhorse_erp.module.order.domain.Order;
import com.inaing.blackhorse_erp.module.order.domain.OrderItem;
import com.inaing.blackhorse_erp.module.order.domain.enums.OrderStatus;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderItemRequestDto;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderUpdateRequestDto;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderResponseDto;
import com.inaing.blackhorse_erp.module.order.mapper.OrderMapper;
import com.inaing.blackhorse_erp.module.order.service.IOrderService;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariantSize;
import com.inaing.blackhorse_erp.module.product.service.IProductVariantSizeService;
import com.inaing.blackhorse_erp.module.role.domain.Role;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;
import com.inaing.blackhorse_erp.security.context.CurrentUserProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateOrderUsecase {

    private final CurrentUserProvider currentUserProvider;
    private final IOrderService orderService;
    private final OrderMapper orderMapper;
    private final IProductVariantSizeService variantSizeService;

    @Transactional
    public OrderResponseDto execute(String identifier, OrderUpdateRequestDto request) {

        Order order = orderService.getByIdentifier(identifier);
        if (order == null) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessRuleException(
                    "ORDER_NOT_EDITABLE",
                    "Only pending orders can be updated.");
        }

        AuthPrincipal editor = currentUserProvider.currentPrincipal()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        Role role = Role.fromName(editor.role());

        List<OrderItemRequestDto> items = dedupeItems(request.items());

        switch (role) {
            case RETAILER -> {
                if (!order.getRetailer().getId().equals(editor.id())) {
                    throw new AppException(ErrorCode.ORDER_UPDATE_DENIED);
                }
                updateItems(order, items, true);
            }
            case SALES -> {
                if (!order.getHandledBy().getId().equals(editor.id())) {
                    throw new AppException(ErrorCode.ORDER_UPDATE_DENIED);
                }
                updateItems(order, items, false);
            }
            case ADMIN -> updateItems(order, items, false);
            default -> throw new AppException(ErrorCode.ORDER_UPDATE_DENIED);
        }

        if (request.note() != null) {
            order.setNote(request.note());
        }
        order.recalculateTotals();

        return orderMapper.toResponse(orderService.update(order));
    }

    private List<OrderItemRequestDto> dedupeItems(List<OrderItemRequestDto> items) {
        Map<String, Integer> merged = new LinkedHashMap<>();

        for (OrderItemRequestDto item : items) {
            merged.merge(
                    item.variantSizeId(),
                    item.quantity(),
                    (existing, incoming) -> existing + incoming);
        }

        return merged.entrySet()
                .stream()
                .map(entry -> new OrderItemRequestDto(entry.getKey(), entry.getValue()))
                .toList();
    }

    private void updateItems(
            Order order,
            List<OrderItemRequestDto> requests,
            boolean retailerUpdate) {

        Map<String, OrderItem> existingItems = order.getItems()
                .stream()
                .collect(Collectors.toMap(
                        item -> item.getVariantSize().getId(),
                        Function.identity()));

        for (OrderItemRequestDto request : requests) {
            OrderItem item = existingItems.remove(request.variantSizeId());
            if (item == null) {
                ProductVariantSize variant = variantSizeService.getById(request.variantSizeId());
                item = OrderItem.builder()
                        .variantSize(variant)
                        .build();
                order.addItem(item);
            }

            if (retailerUpdate) {
                item.setRequestedQuantity(request.quantity());
            }
            item.setQuantity(request.quantity());
        }

        if (retailerUpdate) {
            existingItems.values().forEach(order::removeItem);
        } else {
            existingItems.values().forEach(item -> item.setQuantity(0));
        }
    }
}
