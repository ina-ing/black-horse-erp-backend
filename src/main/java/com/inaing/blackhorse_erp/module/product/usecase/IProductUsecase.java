package com.inaing.blackhorse_erp.module.product.usecase;

import java.util.List;

import com.inaing.blackhorse_erp.module.product.dto.request.ProductCreationRequestDto;
import com.inaing.blackhorse_erp.module.product.dto.request.ProductStatusUpdateRequestDto;
import com.inaing.blackhorse_erp.module.product.dto.request.ProductUpdateRequestDto;
import com.inaing.blackhorse_erp.module.product.dto.response.ProductResponseDto;
import com.inaing.blackhorse_erp.module.product.dto.response.ProductVariantResponseDto;
import com.inaing.blackhorse_erp.module.product.dto.response.ProductVariantSizeResponseDto;

public interface IProductUsecase {

    ProductResponseDto create(ProductCreationRequestDto request);

    ProductResponseDto getByIdentifier(String identifier);

    List<ProductResponseDto> getAll();

    ProductResponseDto update(String id, ProductUpdateRequestDto request);

    ProductResponseDto updateStatus(String id, ProductStatusUpdateRequestDto request);

    ProductVariantResponseDto updateVariantStatus(String variantId, ProductStatusUpdateRequestDto request);

    ProductVariantSizeResponseDto updateVariantSizeStatus(String variantSizeId, ProductStatusUpdateRequestDto request);
}
