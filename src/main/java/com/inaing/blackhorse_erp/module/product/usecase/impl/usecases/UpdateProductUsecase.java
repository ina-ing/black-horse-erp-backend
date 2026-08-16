package com.inaing.blackhorse_erp.module.product.usecase.impl.usecases;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.category.domain.Category;
import com.inaing.blackhorse_erp.module.category.service.ICategoryService;
import com.inaing.blackhorse_erp.module.product.domain.Product;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariant;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariantSize;
import com.inaing.blackhorse_erp.module.product.domain.enums.ProductStatus;
import com.inaing.blackhorse_erp.module.product.dto.request.ProductUpdateRequestDto;
import com.inaing.blackhorse_erp.module.product.dto.request.ProductVariantUpdateRequestDto;
import com.inaing.blackhorse_erp.module.product.dto.response.ProductResponseDto;
import com.inaing.blackhorse_erp.module.product.mapper.ProductMapper;
import com.inaing.blackhorse_erp.module.product.service.IProductService;
import com.inaing.blackhorse_erp.module.storage.service.IStorageService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateProductUsecase {

    private final ICategoryService categoryService;
    private final IProductService productService;
    private final ProductMapper productMapper;
    private final IStorageService storageService;

    private static final String PRODUCT_IMAGE_FOLDER = "products";

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
                variant.setStatus(ProductStatus.ACTIVE);
                keptVariantIds.add(variant.getId());
            }
            String previousImageUrl = variant.getImageUrl();
            String imageUrl = resolveImageUrl(variantRequest);
            variant.setImageUrl(imageUrl);

            if (StringUtils.hasText(previousImageUrl) && !previousImageUrl.equals(imageUrl)) {
                deleteAfterCommit(previousImageUrl);
            }

            syncSizes(product, variant, variantRequest.availableSizes());
        }

        existingVariants.values().stream()
                .filter(variant -> !keptVariantIds.contains(variant.getId()))
                .forEach(variant -> variant.setStatus(ProductStatus.INACTIVE));
    }

    private void syncSizes(Product product, ProductVariant variant, List<String> requestedSizes) {
        Set<String> requested = Set.copyOf(requestedSizes);

        variant.getSizes().forEach(size -> size.setStatus(
                requested.contains(size.getSize()) ? ProductStatus.ACTIVE : ProductStatus.INACTIVE));

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

    private void deleteAfterCommit(String url) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            storageService.deleteByUrl(url);
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                storageService.deleteByUrl(url);
            }
        });
    }

    private String resolveImageUrl(ProductVariantUpdateRequestDto variantRequest) {
        MultipartFile image = variantRequest.image();

        if (image != null && !image.isEmpty()) {
            return storageService.upload(image, PRODUCT_IMAGE_FOLDER);
        }

        return variantRequest.imageUrl();
    }

    private String buildSku(String articleCode, String color, String size) {
        return (articleCode + "-" + color + "-" + size)
                .toUpperCase()
                .replace(" ", "");
    }
}
