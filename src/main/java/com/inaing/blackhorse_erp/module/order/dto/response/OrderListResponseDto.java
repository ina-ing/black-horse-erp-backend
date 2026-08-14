package com.inaing.blackhorse_erp.module.order.dto.response;

import java.time.Instant;

import com.inaing.blackhorse_erp.module.order.domain.enums.OrderStatus;

public record OrderListResponseDto(

        String id,
        String code,
        String retailer,
        String handledBy,
        OrderStatus status,
        Instant orderDate,
        Integer totalArticles,
        Integer totalQuantity) {

}
