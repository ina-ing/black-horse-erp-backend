package com.inaing.blackhorse_erp.module.product.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.inaing.blackhorse_erp.module.product.domain.Material;
import com.inaing.blackhorse_erp.module.product.domain.Product;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariant;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariantSize;
import com.inaing.blackhorse_erp.module.product.dto.request.ProductCreationRequestDto;
import com.inaing.blackhorse_erp.module.product.dto.response.MaterialDto;
import com.inaing.blackhorse_erp.module.product.dto.response.ProductResponseDto;
import com.inaing.blackhorse_erp.module.product.dto.response.ProductVariantResponseDto;
import com.inaing.blackhorse_erp.module.product.dto.response.ProductVariantSizeResponseDto;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "category", source = "category.name")
    ProductResponseDto toResponse(Product product);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "variants", ignore = true)
    Product toEntity(ProductCreationRequestDto request);

    Material toMaterial(MaterialDto dto);

    @Mapping(target = "availableSizes", source = "sizes")
    ProductVariantResponseDto toVariantResponse(ProductVariant variant);

    ProductVariantSizeResponseDto toVariantSizeResponse(ProductVariantSize size);
}
