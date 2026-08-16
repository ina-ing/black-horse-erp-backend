package com.inaing.blackhorse_erp.module.supply.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.inaing.blackhorse_erp.module.supply.domain.Supply;
import com.inaing.blackhorse_erp.module.supply.domain.SupplyItem;
import com.inaing.blackhorse_erp.module.supply.dto.response.SupplyItemResponseDto;
import com.inaing.blackhorse_erp.module.supply.dto.response.SupplyResponseDto;

@Mapper(componentModel = "spring")
public interface SupplyMapper {

    @Mapping(target = "suppliedBy", source = "suppliedBy.name")
    @Mapping(target = "suppliedTo", source = "suppliedTo.name")
    SupplyResponseDto toResponse(Supply supply);

    @Mapping(target = "sku", source = "variantSize.sku")
    @Mapping(target = "size", source = "variantSize.size")
    @Mapping(target = "variantSizeId", source = "variantSize.id")
    @Mapping(target = "color", source = "variantSize.productVariant.color")
    @Mapping(target = "articleCode", source = "variantSize.productVariant.product.articleCode")
    @Mapping(target = "articleName", source = "variantSize.productVariant.product.name")
    SupplyItemResponseDto toItemResponse(SupplyItem item);
}
