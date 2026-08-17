package com.inaing.blackhorse_erp.module.productionOrder.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.inaing.blackhorse_erp.module.productionOrder.domain.ProductionOrder;
import com.inaing.blackhorse_erp.module.productionOrder.domain.ProductionOrderItem;
import com.inaing.blackhorse_erp.module.productionOrder.dto.response.ProductionOrderItemResponseDto;
import com.inaing.blackhorse_erp.module.productionOrder.dto.response.ProductionOrderListResponseDto;
import com.inaing.blackhorse_erp.module.productionOrder.dto.response.ProductionOrderResponseDto;

@Mapper(componentModel = "spring")
public interface ProductionOrderMapper {

    @Mapping(target = "warehouse", source = "warehouse.name")
    ProductionOrderResponseDto toResponse(ProductionOrder order);

    @Mapping(target = "warehouse", source = "warehouse.name")
    ProductionOrderListResponseDto toListResponse(ProductionOrder order);

    @Mapping(target = "sku", source = "variantSize.sku")
    @Mapping(target = "size", source = "variantSize.size")
    @Mapping(target = "variantSizeId", source = "variantSize.id")
    @Mapping(target = "color", source = "variantSize.productVariant.color")
    @Mapping(target = "articleCode", source = "variantSize.productVariant.product.articleCode")
    @Mapping(target = "articleName", source = "variantSize.productVariant.product.name")
    ProductionOrderItemResponseDto toItemResponse(ProductionOrderItem item);
}
