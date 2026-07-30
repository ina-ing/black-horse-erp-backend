package com.inaing.blackhorse_erp.module.category.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.module.category.domain.Category;
import com.inaing.blackhorse_erp.module.category.dto.CategoryCreationDto;
import com.inaing.blackhorse_erp.module.category.dto.CategoryResponseDto;
import com.inaing.blackhorse_erp.module.category.mapper.CategoryMapper;
import com.inaing.blackhorse_erp.module.category.service.ICategoryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateCategoryUsecase {

    private final ICategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryResponseDto execute(CategoryCreationDto request) {
        Category category = Category.builder().name(request.name()).build();

        return categoryMapper.toResponse(categoryService.create(category));
    }
}
