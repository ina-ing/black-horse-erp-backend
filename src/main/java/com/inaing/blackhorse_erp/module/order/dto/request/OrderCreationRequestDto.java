package com.inaing.blackhorse_erp.module.order.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record OrderCreationRequestDto(
        @NotBlank String retailer,
        String note,
        @NotEmpty @Valid List<OrderItemRequestDto> items) {

}
