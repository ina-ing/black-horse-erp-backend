package com.inaing.blackhorse_erp.module.inventory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.inaing.blackhorse_erp.module.inventory.domain.Inventory;
import com.inaing.blackhorse_erp.module.inventory.domain.InventoryItem;
import com.inaing.blackhorse_erp.module.inventory.dto.response.InventoryItemResponseDto;
import com.inaing.blackhorse_erp.module.inventory.dto.response.InventoryResponseDto;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    InventoryResponseDto toResponse(Inventory inventory);

    @Mapping(target = "sku", source = "variantSize.sku")
    @Mapping(target = "size", source = "variantSize.size")
    @Mapping(target = "variantSizeId", source = "variantSize.id")
    @Mapping(target = "color", source = "variantSize.productVariant.color")
    @Mapping(target = "articleName", source = "variantSize.productVariant.product.name")
    InventoryItemResponseDto toItemResponse(InventoryItem item);
}
