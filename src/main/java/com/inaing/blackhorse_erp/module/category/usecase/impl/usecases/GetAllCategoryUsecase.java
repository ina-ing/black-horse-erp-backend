package com.inaing.blackhorse_erp.module.category.usecase.impl.usecases;

import java.util.List;

import org.springframework.stereotype.Component;

import com.inaing.blackhorse_erp.module.category.dto.CategoryResponseDto;
import com.inaing.blackhorse_erp.module.category.mapper.CategoryMapper;
import com.inaing.blackhorse_erp.module.category.service.ICategoryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetAllCategoryUsecase {

    private final CategoryMapper categoryMapper;
    private final ICategoryService categoryService;

    public List<CategoryResponseDto> execute() {

        return categoryService.getAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }
}
