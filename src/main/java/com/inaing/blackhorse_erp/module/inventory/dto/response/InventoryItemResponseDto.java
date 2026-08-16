package com.inaing.blackhorse_erp.module.inventory.dto.response;

public record InventoryItemResponseDto(

                String id,
                String articleCode,
                String articleName,
                String variantSizeId,
                String color,
                String sku,
                String size,
                Integer quantity) {

}
