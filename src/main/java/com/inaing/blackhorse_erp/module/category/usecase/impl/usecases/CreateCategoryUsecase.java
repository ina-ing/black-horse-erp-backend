package com.inaing.blackhorse_erp.module.category.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.module.category.domain.Category;
import com.inaing.blackhorse_erp.module.category.dto.CategoryRequestDto;
import com.inaing.blackhorse_erp.module.category.dto.CategoryResponseDto;
import com.inaing.blackhorse_erp.module.category.mapper.CategoryMapper;
import com.inaing.blackhorse_erp.module.category.service.ICategoryService;

import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityAction;
import com.inaing.blackhorse_erp.module.activityLog.domain.enums.ActivityEntityType;
import com.inaing.blackhorse_erp.module.activityLog.service.IActivityLogService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateCategoryUsecase {

    private final ICategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private final IActivityLogService activityLogService;

    @Transactional
    public CategoryResponseDto execute(CategoryRequestDto request) {
        Category category = Category.builder()
                .name(request.name())
                .build();

        Category created = categoryService.create(category);

        activityLogService.record(
                ActivityAction.CATEGORY_CREATED,
                "Created category " + created.getName() + ".",
                ActivityEntityType.CATEGORY, created.getId(), created.getName(),
                ActionTrigger.CREATION);

        return categoryMapper.toResponse(created);
    }
}
