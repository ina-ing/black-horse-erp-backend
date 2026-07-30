package com.inaing.blackhorse_erp.module.order.usecase.impl.usecases;

import java.util.List;

import org.springframework.stereotype.Component;

import com.inaing.blackhorse_erp.module.order.dto.response.OrderResponseDto;
import com.inaing.blackhorse_erp.module.order.mapper.OrderMapper;
import com.inaing.blackhorse_erp.module.order.service.IOrderService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetOrdersUsecase {

    private final OrderMapper orderMapper;
    private final IOrderService orderService;

    public List<OrderResponseDto> execute() {
        return orderService.getAll()
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }
}
