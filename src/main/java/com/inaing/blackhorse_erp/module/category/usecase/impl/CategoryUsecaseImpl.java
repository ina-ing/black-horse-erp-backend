package com.inaing.blackhorse_erp.module.category.usecase.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.inaing.blackhorse_erp.module.category.dto.CategoryRequestDto;
import com.inaing.blackhorse_erp.module.category.dto.CategoryResponseDto;
import com.inaing.blackhorse_erp.module.category.usecase.ICategoryUsecase;
import com.inaing.blackhorse_erp.module.category.usecase.impl.usecases.CreateCategoryUsecase;
import com.inaing.blackhorse_erp.module.category.usecase.impl.usecases.GetAllCategoryUsecase;
import com.inaing.blackhorse_erp.module.category.usecase.impl.usecases.UpdateCategoryUsecase;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryUsecaseImpl implements ICategoryUsecase {

    private final CreateCategoryUsecase createCategoryUsecase;
    private final UpdateCategoryUsecase updateCategoryUsecase;
    private final GetAllCategoryUsecase getAllCategoryUsecase;

    @Override
    public CategoryResponseDto create(CategoryRequestDto request) {
        return createCategoryUsecase.execute(request);
    }

    @Override
    public CategoryResponseDto update(String identifier, CategoryRequestDto request) {
        return updateCategoryUsecase.execute(identifier, request);
    }

    @Override
    public List<CategoryResponseDto> getAll() {
        return getAllCategoryUsecase.execute();
    }

}
