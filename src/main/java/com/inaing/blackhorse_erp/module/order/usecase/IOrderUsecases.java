package com.inaing.blackhorse_erp.module.order.usecase;

import java.util.List;

import com.inaing.blackhorse_erp.module.order.dto.request.OrderCreationRequestDto;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderFulfillmentRequestDto;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderStatusUpdateRequestDto;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderUpdateRequestDto;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderResponseDto;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderWithStatusHistoryResponseDto;

public interface IOrderUsecases {

    OrderResponseDto create(OrderCreationRequestDto request);

    OrderWithStatusHistoryResponseDto getByIdentifier(String code);

    List<OrderResponseDto> getAll();

    OrderResponseDto update(String id, OrderUpdateRequestDto request);

    OrderResponseDto fulfill(String id, OrderFulfillmentRequestDto request);

    OrderResponseDto updateStatus(String id, OrderStatusUpdateRequestDto request);
}