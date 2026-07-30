package com.inaing.blackhorse_erp.module.category.usecase.impl;

import org.springframework.stereotype.Component;

import com.inaing.blackhorse_erp.module.category.dto.CategoryCreationDto;
import com.inaing.blackhorse_erp.module.category.dto.CategoryResponseDto;
import com.inaing.blackhorse_erp.module.category.usecase.ICategoryUsecase;
import com.inaing.blackhorse_erp.module.category.usecase.impl.usecases.CreateCategoryUsecase;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryUsecaseImpl implements ICategoryUsecase {

    private final CreateCategoryUsecase createCategoryUsecase;

    @Override
    public CategoryResponseDto create(CategoryCreationDto request) {
        return createCategoryUsecase.execute(request);
    }

}
