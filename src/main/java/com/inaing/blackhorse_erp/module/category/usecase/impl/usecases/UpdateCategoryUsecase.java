package com.inaing.blackhorse_erp.module.category.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.category.domain.Category;
import com.inaing.blackhorse_erp.module.category.dto.CategoryRequestDto;
import com.inaing.blackhorse_erp.module.category.dto.CategoryResponseDto;
import com.inaing.blackhorse_erp.module.category.mapper.CategoryMapper;
import com.inaing.blackhorse_erp.module.category.service.ICategoryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateCategoryUsecase {

    private final ICategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryResponseDto execute(String identifier, CategoryRequestDto request) {
        Category category = categoryService.getByIdentifier(identifier);
        if (category == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "Category not found " + identifier);
        }

        categoryMapper.updateEntity(request, category);

        return categoryMapper.toResponse(categoryService.update(category));
    }
}
