package com.inaing.blackhorse_erp.module.category.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.inaing.blackhorse_erp.common.dto.ApiResponse;
import com.inaing.blackhorse_erp.module.category.dto.CategoryRequestDto;
import com.inaing.blackhorse_erp.module.category.dto.CategoryResponseDto;
import com.inaing.blackhorse_erp.module.category.usecase.ICategoryUsecase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryUsecase categoryUsecase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'FACTORY')")
    public ApiResponse<CategoryResponseDto> create(@RequestBody CategoryRequestDto request) {
        return ApiResponse.created("Category created", categoryUsecase.create(request));
    }

    @PutMapping("/{identifier}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACTORY')")
    public ApiResponse<CategoryResponseDto> update(@PathVariable String identifier,
            @Valid @RequestBody CategoryRequestDto request) {
        return ApiResponse.ok("Category updated", categoryUsecase.update(identifier, request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SALES','RETAILER','WAREHOUSE','FACTORY')")
    public ApiResponse<List<CategoryResponseDto>> getAll() {
        return ApiResponse.ok(categoryUsecase.getAll());
    }

}
