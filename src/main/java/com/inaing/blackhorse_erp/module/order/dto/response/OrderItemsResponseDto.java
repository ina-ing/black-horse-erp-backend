package com.inaing.blackhorse_erp.module.order.dto.response;

public record OrderItemsResponseDto(
        String id,
        String variantSizeId,
        String sku,
        String size,
        Integer quantity,
        Integer requestedQuantity,
        Integer fulfilledQuantity) {

}
