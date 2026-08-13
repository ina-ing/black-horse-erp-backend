package com.inaing.blackhorse_erp.module.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.inaing.blackhorse_erp.module.category.domain.Category;
import com.inaing.blackhorse_erp.module.category.dto.CategoryRequestDto;
import com.inaing.blackhorse_erp.module.category.dto.CategoryResponseDto;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponseDto toResponse(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "identifier", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntity(CategoryRequestDto request, @MappingTarget Category category);
}
