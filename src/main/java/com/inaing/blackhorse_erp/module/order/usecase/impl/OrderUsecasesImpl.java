package com.inaing.blackhorse_erp.module.order.usecase.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.inaing.blackhorse_erp.module.order.dto.request.OrderCreationRequestDto;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderFulfillmentRequestDto;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderStatusUpdateRequestDto;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderUpdateRequestDto;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderResponseDto;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderWithStatusHistoryResponseDto;
import com.inaing.blackhorse_erp.module.order.usecase.IOrderUsecases;
import com.inaing.blackhorse_erp.module.order.usecase.impl.usecases.CreateOrderUsecase;
import com.inaing.blackhorse_erp.module.order.usecase.impl.usecases.FulfillOrderUsecase;
import com.inaing.blackhorse_erp.module.order.usecase.impl.usecases.GetOrderUsecase;
import com.inaing.blackhorse_erp.module.order.usecase.impl.usecases.GetAllOrdersUsecase;
import com.inaing.blackhorse_erp.module.order.usecase.impl.usecases.UpdateOrderStatusUsecase;
import com.inaing.blackhorse_erp.module.order.usecase.impl.usecases.UpdateOrderUsecase;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderUsecasesImpl implements IOrderUsecases {

    private final CreateOrderUsecase createOrderUsecase;
    private final GetOrderUsecase getOrderUsecase;
    private final GetAllOrdersUsecase getAllOrdersUsecase;
    private final UpdateOrderUsecase updateOrderUsecase;
    private final FulfillOrderUsecase fulfillOrderUsecase;
    private final UpdateOrderStatusUsecase updateOrderStatusUsecase;

    @Override
    public OrderResponseDto create(OrderCreationRequestDto request) {
        return createOrderUsecase.execute(request);
    }

    @Override
    public OrderWithStatusHistoryResponseDto getByIdentifier(String code) {
        return getOrderUsecase.execute(code);
    }

    @Override
    public List<OrderResponseDto> getAll() {
        return getAllOrdersUsecase.execute();
    }

    @Override
    public OrderResponseDto update(String id, OrderUpdateRequestDto request) {
        return updateOrderUsecase.execute(id, request);
    }

    @Override
    public OrderResponseDto fulfill(String id, OrderFulfillmentRequestDto request) {
        return fulfillOrderUsecase.execute(id, request);
    }

    @Override
    public OrderResponseDto updateStatus(String id, OrderStatusUpdateRequestDto request) {
        return updateOrderStatusUsecase.execute(id, request);
    }

}
