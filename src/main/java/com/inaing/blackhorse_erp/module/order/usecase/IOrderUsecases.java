package com.inaing.blackhorse_erp.module.order.usecase;

import com.inaing.blackhorse_erp.common.dto.list.PagedListWithAnalytics;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderCreationRequestDto;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderFulfillmentRequestDto;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderStatusUpdateRequestDto;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderQueryParams;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderUpdateRequestDto;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderListResponseDto;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderResponseDto;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderWithStatusHistoryResponseDto;
import com.inaing.blackhorse_erp.module.order.dto.response.analytics.OrderBasicAnalyticsDto;

public interface IOrderUsecases {

    OrderResponseDto create(OrderCreationRequestDto request);

    OrderWithStatusHistoryResponseDto getByIdentifier(String code);

    PagedListWithAnalytics<OrderBasicAnalyticsDto, OrderListResponseDto> getAll(OrderQueryParams params);

    OrderResponseDto update(String id, OrderUpdateRequestDto request);

    OrderResponseDto fulfill(String id, OrderFulfillmentRequestDto request);

    OrderResponseDto updateStatus(String id, OrderStatusUpdateRequestDto request);
}