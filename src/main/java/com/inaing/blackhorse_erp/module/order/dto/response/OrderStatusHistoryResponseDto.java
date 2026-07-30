package com.inaing.blackhorse_erp.module.order.dto.response;

import java.time.Instant;

import com.inaing.blackhorse_erp.module.order.domain.enums.OrderHistoryTrigger;
import com.inaing.blackhorse_erp.module.order.domain.enums.OrderStatus;

public record OrderStatusHistoryResponseDto(

        String id,
        OrderStatus status,
        OrderHistoryTrigger trigger,
        String actorName,
        String actorRole,
        Instant createdAt) {

}
