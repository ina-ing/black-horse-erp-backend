package com.inaing.blackhorse_erp.module.order.usecase.impl.usecases;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.order.domain.Order;
import com.inaing.blackhorse_erp.module.order.domain.enums.OrderStatus;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderListResponseDto;
import com.inaing.blackhorse_erp.module.order.mapper.OrderMapper;
import com.inaing.blackhorse_erp.module.order.service.IOrderService;
import com.inaing.blackhorse_erp.module.role.domain.Role;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;
import com.inaing.blackhorse_erp.security.context.CurrentUserProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetAllOrdersUsecase {

    private final OrderMapper orderMapper;
    private final IOrderService orderService;
    private final CurrentUserProvider currentUserProvider;

    @Transactional(readOnly = true)
    public List<OrderListResponseDto> execute(OrderStatus status) {
        List<Order> orders = status != null
                ? orderService.getAllByStatus(status)
                : orderService.getAll();

        AuthPrincipal principal = currentUserProvider.currentPrincipal()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        if (Role.fromName(principal.role()) == Role.SALES) {
            orders = orders.stream()
                    .filter(order -> order.getHandledBy() != null
                            && order.getHandledBy().getId().equals(principal.id()))
                    .toList();
        }
        if (Role.fromName(principal.role()) == Role.RETAILER) {
            orders = orders.stream()
                    .filter(order -> order.getRetailer() != null
                            && order.getRetailer().getId().equals(principal.id()))
                    .toList();
        }

        return orders.stream()
                .map(orderMapper::toListResponse)
                .toList();
    }
}
