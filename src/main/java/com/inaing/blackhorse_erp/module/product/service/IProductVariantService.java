package com.inaing.blackhorse_erp.module.product.service;

import com.inaing.blackhorse_erp.module.product.domain.ProductVariant;

public interface IProductVariantService {

    ProductVariant getById(String id);

    ProductVariant update(ProductVariant variant);
}
