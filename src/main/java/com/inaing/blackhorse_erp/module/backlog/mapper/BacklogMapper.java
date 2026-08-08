package com.inaing.blackhorse_erp.module.backlog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.inaing.blackhorse_erp.module.backlog.domain.Backlog;
import com.inaing.blackhorse_erp.module.backlog.domain.BacklogItem;
import com.inaing.blackhorse_erp.module.backlog.dto.response.BacklogItemResponseDto;
import com.inaing.blackhorse_erp.module.backlog.dto.response.BacklogResponseDto;

@Mapper(componentModel = "spring")
public interface BacklogMapper {

    @Mapping(target = "factory", source = "factory.name")
    BacklogResponseDto toResponse(Backlog backlog);

    @Mapping(target = "sku", source = "variantSize.sku")
    @Mapping(target = "size", source = "variantSize.size")
    @Mapping(target = "variantSizeId", source = "variantSize.id")
    @Mapping(target = "color", source = "variantSize.productVariant.color")
    @Mapping(target = "articleName", source = "variantSize.productVariant.product.name")
    BacklogItemResponseDto toItemResponse(BacklogItem item);
}
