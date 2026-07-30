package com.inaing.blackhorse_erp.module.product.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.inaing.blackhorse_erp.common.dto.ApiResponse;
import com.inaing.blackhorse_erp.module.product.dto.request.ProductCreationRequestDto;
import com.inaing.blackhorse_erp.module.product.dto.request.ProductUpdateRequestDto;
import com.inaing.blackhorse_erp.module.product.dto.response.ProductResponseDto;
import com.inaing.blackhorse_erp.module.product.usecase.IProductUsecase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final IProductUsecase productUsecase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProductResponseDto> create(@Valid @RequestBody ProductCreationRequestDto request) {
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

    @PutMapping("/{id}")
    public ApiResponse<ProductResponseDto> update(@PathVariable String id,
            @Valid @RequestBody ProductUpdateRequestDto request) {
        return ApiResponse.created("Product updated", productUsecase.update(id, request));
    }
}
