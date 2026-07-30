package com.inaing.blackhorse_erp.module.order.usecase.impl.usecases;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.order.domain.Order;
import com.inaing.blackhorse_erp.module.order.domain.OrderStatusHistory;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderStatusHistoryResponseDto;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderWithStatusHistoryResponseDto;
import com.inaing.blackhorse_erp.module.order.mapper.OrderMapper;
import com.inaing.blackhorse_erp.module.order.service.IOrderService;
import com.inaing.blackhorse_erp.module.order.service.IOrderStatusHistoryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetOrderIdentifierUsecase {

    private final OrderMapper orderMapper;
    private final IOrderService orderService;
    private final IOrderStatusHistoryService orderStatusHistoryService;

    @Transactional(readOnly = true)
    public OrderWithStatusHistoryResponseDto execute(String code) {

        Order order = orderService.getByIdentifier(code);

        if (order == null) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND, "Order not found " + code);
        }

        List<OrderStatusHistory> history = orderStatusHistoryService.getByOrder(order.getId());
        List<OrderStatusHistoryResponseDto> historyResponse = orderMapper.toHistoryResponseList(history);

        return orderMapper.toResponseWithHistory(order, historyResponse);
    }
}
