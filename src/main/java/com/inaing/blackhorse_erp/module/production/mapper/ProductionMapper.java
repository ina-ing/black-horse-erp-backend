package com.inaing.blackhorse_erp.module.production.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.inaing.blackhorse_erp.module.production.domain.Production;
import com.inaing.blackhorse_erp.module.production.domain.ProductionItem;
import com.inaing.blackhorse_erp.module.production.dto.response.ProductionItemsResponseDto;
import com.inaing.blackhorse_erp.module.production.dto.response.ProductionListResponseDto;
import com.inaing.blackhorse_erp.module.production.dto.response.ProductionResponseDto;

@Mapper(componentModel = "spring")
public interface ProductionMapper {

    @Mapping(target = "factory", source = "factory.name")
    ProductionResponseDto toResponse(Production production);

    @Mapping(target = "factory", source = "factory.name")
    ProductionListResponseDto toListResponse(Production production);

    @Mapping(target = "sku", source = "variantSize.sku")
    @Mapping(target = "size", source = "variantSize.size")
    @Mapping(target = "variantSizeId", source = "variantSize.id")
    @Mapping(target = "color", source = "variantSize.productVariant.color")
    @Mapping(target = "articleCode", source = "variantSize.productVariant.product.articleCode")
    @Mapping(target = "articleName", source = "variantSize.productVariant.product.name")
    ProductionItemsResponseDto toItemResponse(ProductionItem item);
}