package com.inaing.blackhorse_erp.module.order.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.inaing.blackhorse_erp.module.order.domain.Order;
import com.inaing.blackhorse_erp.module.order.domain.OrderItem;
import com.inaing.blackhorse_erp.module.order.domain.OrderStatusHistory;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderItemsResponseDto;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderResponseDto;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderStatusHistoryResponseDto;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderWithStatusHistoryResponseDto;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "retailer", source = "retailer.storeName")
    @Mapping(target = "handledBy", source = "handledBy.fullname")
    OrderResponseDto toResponse(Order order);

    @Mapping(target = "retailer", source = "order.retailer.storeName")
    @Mapping(target = "handledBy", source = "order.handledBy.fullname")
    OrderWithStatusHistoryResponseDto toResponseWithHistory(Order order,
            List<OrderStatusHistoryResponseDto> statusHistory);

    @Mapping(target = "variantSizeId", source = "variantSize.id")
    @Mapping(target = "sku", source = "variantSize.sku")
    @Mapping(target = "size", source = "variantSize.size")
    OrderItemsResponseDto toItemResponse(OrderItem item);

    OrderStatusHistoryResponseDto toHistoryResponse(OrderStatusHistory history);

    List<OrderStatusHistoryResponseDto> toHistoryResponseList(List<OrderStatusHistory> histories);
}
