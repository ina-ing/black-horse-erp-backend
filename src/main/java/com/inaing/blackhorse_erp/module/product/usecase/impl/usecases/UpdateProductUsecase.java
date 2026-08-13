package com.inaing.blackhorse_erp.module.product.usecase.impl.usecases;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.category.domain.Category;
import com.inaing.blackhorse_erp.module.category.service.ICategoryService;
import com.inaing.blackhorse_erp.module.product.domain.Product;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariant;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariantSize;
import com.inaing.blackhorse_erp.module.product.dto.request.ProductUpdateRequestDto;
import com.inaing.blackhorse_erp.module.product.dto.request.ProductVariantUpdateRequestDto;
import com.inaing.blackhorse_erp.module.product.dto.response.ProductResponseDto;
import com.inaing.blackhorse_erp.module.product.mapper.ProductMapper;
import com.inaing.blackhorse_erp.module.product.service.IProductService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateProductUsecase {

    private final ICategoryService categoryService;
    private final IProductService productService;
    private final ProductMapper productMapper;

    @Transactional
    public ProductResponseDto execute(String id, ProductUpdateRequestDto request) {
        Product product = productService.getById(id);

        Category category = categoryService.getByIdentifier(request.category());
        if (category == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "No category with name: " + request.category());
        }

        product.setArticleCode(request.articleCode());
        product.setName(request.name());
        product.setCategory(category);
        product.setGender(request.gender());
        product.setSizeSystem(request.sizeSystem());
        product.setStatus(request.status());
        product.setMaterial(productMapper.toMaterial(request.material()));

        syncVariants(product, request.variants());

        return productMapper.toResponse(productService.update(product));
    }

    private void syncVariants(Product product, List<ProductVariantUpdateRequestDto> variantRequests) {
        Map<String, ProductVariant> existingVariants = product.getVariants().stream()
                .collect(Collectors.toMap(ProductVariant::getId, Function.identity()));

        Set<String> keptVariantIds = new HashSet<>();

        for (ProductVariantUpdateRequestDto variantRequest : variantRequests) {
            ProductVariant variant = variantRequest.id() != null ? existingVariants.get(variantRequest.id()) : null;

            if (variant == null) {
                variant = ProductVariant.builder().color(variantRequest.color()).build();
                product.addVariant(variant);
            } else {
                variant.setColor(variantRequest.color());
                keptVariantIds.add(variant.getId());
            }
            variant.setImageUrl(variantRequest.imageUrl());

            syncSizes(product, variant, variantRequest.availableSizes());
        }

        existingVariants.values().stream()
                .filter(variant -> !keptVariantIds.contains(variant.getId()))
                .toList()
                .forEach(product::removeVariant);
    }

    private void syncSizes(Product product, ProductVariant variant, List<String> requestedSizes) {
        Set<String> requested = Set.copyOf(requestedSizes);

        variant.getSizes().stream()
                .filter(size -> !requested.contains(size.getSize()))
                .toList()
                .forEach(variant::removeSize);

        Set<String> existingSizes = variant.getSizes().stream()
                .map(ProductVariantSize::getSize)
                .collect(Collectors.toSet());

        requested.stream()
                .filter(size -> !existingSizes.contains(size))
                .forEach(size -> variant.addSize(ProductVariantSize.builder()
                        .size(size)
                        .sku(buildSku(product.getArticleCode(), variant.getColor(), size))
                        .build()));
    }

    private String buildSku(String articleCode, String color, String size) {
        return (articleCode + "-" + color + "-" + size)
                .toUpperCase()
                .replace(" ", "");
    }
}
