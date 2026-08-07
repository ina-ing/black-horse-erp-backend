package com.inaing.blackhorse_erp.module.order.dto.request;

import com.inaing.blackhorse_erp.module.order.domain.enums.OrderStatus;

import jakarta.validation.constraints.NotNull;

public record OrderStatusUpdateRequestDto(

        @NotNull OrderStatus status) {
}
