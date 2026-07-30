package com.inaing.blackhorse_erp.module.product.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.category.domain.Category;
import com.inaing.blackhorse_erp.module.category.service.ICategoryService;
import com.inaing.blackhorse_erp.module.product.domain.Product;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariant;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariantSize;
import com.inaing.blackhorse_erp.module.product.dto.request.ProductCreationRequestDto;
import com.inaing.blackhorse_erp.module.product.dto.response.ProductResponseDto;
import com.inaing.blackhorse_erp.module.product.mapper.ProductMapper;
import com.inaing.blackhorse_erp.module.product.service.IProductService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateProductUsecase {

    private final ProductMapper productMapper;
    private final ICategoryService categoryService;
    private final IProductService productService;

    @Transactional
    public ProductResponseDto execute(ProductCreationRequestDto request) {
        Category category = categoryService.getByIdentifier(request.category());
        if (category == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "No category with name: " + request.category());
        }

        Product product = productMapper.toEntity(request);
        product.setCategory(category);

        request.variants().forEach(v -> {
            ProductVariant variant = ProductVariant.builder()
                    .color(v.color())
                    .build();
            product.addVariant(variant);

            v.availableSizes().forEach(size -> {
                ProductVariantSize variantSize = ProductVariantSize.builder()
                        .size(size)
                        .sku(buildSku(product.getArticleCode(), v.color(), size))
                        .build();
                variant.addSize(variantSize);
            });
        });

        return productMapper.toResponse(productService.create(product));
    }

    private String buildSku(String articleCode, String color, String size) {
        return (articleCode + "-" + color + "-" + size)
                .toUpperCase()
                .replace(" ", "");
    }
}