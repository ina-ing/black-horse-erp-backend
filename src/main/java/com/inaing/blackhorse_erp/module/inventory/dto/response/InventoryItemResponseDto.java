package com.inaing.blackhorse_erp.module.inventory.dto.response;

public record InventoryItemResponseDto(

        String id,
        String articleName,
        String variantSizeId,
        String color,
        String sku,
        String size,
        Integer quantity) {

}
