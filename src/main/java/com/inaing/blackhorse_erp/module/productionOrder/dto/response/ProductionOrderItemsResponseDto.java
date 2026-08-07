package com.inaing.blackhorse_erp.module.productionOrder.dto.response;

public record ProductionOrderItemsResponseDto(

    String id,
    String articleName,
    String variantSizeId,
    String color,
    String sku,
    String size,
    Integer quantity
) {
    
}
