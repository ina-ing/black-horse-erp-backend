package com.inaing.blackhorse_erp.module.category.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.category.domain.Category;
import com.inaing.blackhorse_erp.module.category.repository.CategoryRepository;
import com.inaing.blackhorse_erp.module.category.service.ICategoryService;
import com.inaing.blackhorse_erp.utils.StringUtils;
import com.inaing.blackhorse_erp.utils.uuid.UUIDUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public Category getByIdentifier(String identifier) {
        if (UUIDUtils.isUUID(identifier)) {
            return categoryRepository.findById(identifier).orElse(null);
        }
        return categoryRepository
                .findByNameIgnoreCaseOrIdentifierIgnoreCase(identifier, StringUtils.generateIdentifier(identifier))
                .orElse(null);
    }

    @Override
    public Category create(Category category) {
        String identifier = StringUtils.generateIdentifier(category.getName());

        if (categoryRepository.existsByIdentifier(identifier)) {
            throw new AppException(ErrorCode.DUPLICATE_RESOURCE, "Category already exists " + category.getName());
        }
        category.setIdentifier(identifier);
        return categoryRepository.save(category);
    }

}
