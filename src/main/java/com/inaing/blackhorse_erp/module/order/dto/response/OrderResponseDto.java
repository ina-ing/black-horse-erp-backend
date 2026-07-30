package com.inaing.blackhorse_erp.module.order.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.inaing.blackhorse_erp.module.order.domain.enums.OrderStatus;

public record OrderResponseDto(

        String id,
        String code,
        String retailer,
        String handledBy,
        OrderStatus status,
        LocalDate orderDate,
        Integer totalArticles,
        Integer totalQuantity,
        String note,
        List<OrderItemsResponseDto> items) {

}
