package com.inaing.blackhorse_erp.module.category.usecase;

import java.util.List;

import com.inaing.blackhorse_erp.module.category.dto.CategoryRequestDto;
import com.inaing.blackhorse_erp.module.category.dto.CategoryResponseDto;

public interface ICategoryUsecase {

    CategoryResponseDto create(CategoryRequestDto request);

    CategoryResponseDto update(String identifier, CategoryRequestDto request);

    List<CategoryResponseDto> getAll();

}
