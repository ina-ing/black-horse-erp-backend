package com.inaing.blackhorse_erp.module.category.usecase;

import com.inaing.blackhorse_erp.module.category.dto.CategoryCreationDto;
import com.inaing.blackhorse_erp.module.category.dto.CategoryResponseDto;

public interface ICategoryUsecase {
    CategoryResponseDto create(CategoryCreationDto request);
}
