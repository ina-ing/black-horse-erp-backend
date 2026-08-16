package com.inaing.blackhorse_erp.module.product.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.inaing.blackhorse_erp.common.dto.ApiResponse;
import com.inaing.blackhorse_erp.module.product.dto.request.ProductCreationRequestDto;
import com.inaing.blackhorse_erp.module.product.dto.request.ProductStatusUpdateRequestDto;
import com.inaing.blackhorse_erp.module.product.dto.request.ProductUpdateRequestDto;
import com.inaing.blackhorse_erp.module.product.dto.response.ProductResponseDto;
import com.inaing.blackhorse_erp.module.product.dto.response.ProductVariantResponseDto;
import com.inaing.blackhorse_erp.module.product.dto.response.ProductVariantSizeResponseDto;
import com.inaing.blackhorse_erp.module.product.usecase.IProductUsecase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final IProductUsecase productUsecase;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','FACTORY')")
    public ApiResponse<ProductResponseDto> create(@Valid @ModelAttribute ProductCreationRequestDto request) {
        return ApiResponse.created("Product created", productUsecase.create(request));
    }

    @GetMapping("/{identifier}")
    public ApiResponse<ProductResponseDto> getById(@PathVariable String identifier) {
        return ApiResponse.ok(productUsecase.getByIdentifier(identifier));
    }

    @GetMapping
    public ApiResponse<List<ProductResponseDto>> getAll() {
        return ApiResponse.ok(productUsecase.getAll());
    }

    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','FACTORY')")
    public ApiResponse<ProductResponseDto> update(@PathVariable String id,
            @Valid @ModelAttribute ProductUpdateRequestDto request) {
        return ApiResponse.created("Product updated", productUsecase.update(id, request));
    }

    @PatchMapping("/status/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FACTORY')")
    public ApiResponse<ProductResponseDto> updateStatus(@PathVariable String id,
            @Valid @RequestBody ProductStatusUpdateRequestDto request) {
        return ApiResponse.ok(productUsecase.updateStatus(id, request));
    }

    @PatchMapping("/variant/status/{variantId}")
    @PreAuthorize("hasAnyRole('ADMIN','FACTORY')")
    public ApiResponse<ProductVariantResponseDto> updateVariantStatus(@PathVariable String variantId,
            @Valid @RequestBody ProductStatusUpdateRequestDto request) {
        return ApiResponse.ok(productUsecase.updateVariantStatus(variantId, request));
    }

    @PatchMapping("/variant-size/status/{variantSizeId}")
    @PreAuthorize("hasAnyRole('ADMIN','FACTORY')")
    public ApiResponse<ProductVariantSizeResponseDto> updateVariantSizeStatus(@PathVariable String variantSizeId,
            @Valid @RequestBody ProductStatusUpdateRequestDto request) {
        return ApiResponse.ok(productUsecase.updateVariantSizeStatus(variantSizeId, request));
    }
}
