package com.inaing.blackhorse_erp.module.order.dto.response;

import java.time.Instant;

import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.module.order.domain.enums.OrderStatus;

public record OrderStatusHistoryResponseDto(

        String id,
        OrderStatus status,
        ActionTrigger trigger,
        String principalName,
        String principalRole,
        Instant createdAt) {

}
