package com.inaing.blackhorse_erp.module.category.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.inaing.blackhorse_erp.common.dto.ApiResponse;
import com.inaing.blackhorse_erp.module.category.dto.CategoryCreationDto;
import com.inaing.blackhorse_erp.module.category.dto.CategoryResponseDto;
import com.inaing.blackhorse_erp.module.category.usecase.ICategoryUsecase;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryUsecase categoryUsecase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CategoryResponseDto> create(@RequestBody CategoryCreationDto request) {
        return ApiResponse.created("Category created", categoryUsecase.create(request));
    }

}
