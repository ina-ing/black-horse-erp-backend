package com.inaing.blackhorse_erp.module.category.service;

import java.util.List;

import com.inaing.blackhorse_erp.module.category.domain.Category;

public interface ICategoryService {

    Category create(Category category);

    Category update(Category category);

    Category getByIdentifier(String identifier);

    List<Category> getAll();
}
