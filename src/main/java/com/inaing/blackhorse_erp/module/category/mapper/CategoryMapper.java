package com.inaing.blackhorse_erp.module.category.mapper;

import org.mapstruct.Mapper;

import com.inaing.blackhorse_erp.module.category.domain.Category;
import com.inaing.blackhorse_erp.module.category.dto.CategoryResponseDto;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponseDto toResponse(Category category);
}
