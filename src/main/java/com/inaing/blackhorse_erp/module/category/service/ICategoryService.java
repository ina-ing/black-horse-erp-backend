package com.inaing.blackhorse_erp.module.category.service;

import com.inaing.blackhorse_erp.module.category.domain.Category;

public interface ICategoryService {

    Category create(Category category);

    Category getByIdentifier(String identifier);

}
