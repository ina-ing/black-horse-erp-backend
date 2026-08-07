package com.inaing.blackhorse_erp.module.order.dto.response;

public record OrderItemsResponseDto(
        String id,
        String articleName,
        String variantSizeId,
        String color,
        String sku,
        String size,
        Integer quantity,
        Integer requestedQuantity,
        Integer fulfilledQuantity) {

}
