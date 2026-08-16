package com.inaing.blackhorse_erp.module.productionOrder.dto.response;

public record ProductionOrderItemResponseDto(

    String id,
    String articleCode,
    String articleName,
    String variantSizeId,
    String color,
    String sku,
    String size,
    Integer quantity
) {
    
}
