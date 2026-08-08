package com.inaing.blackhorse_erp.module.backlog.service;

import java.util.Map;

import com.inaing.blackhorse_erp.module.backlog.domain.Backlog;
import com.inaing.blackhorse_erp.module.factory.domain.Factory;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariantSize;

public interface IBacklogService {

    Backlog create(Factory factory);

    Backlog addQuantities(Factory factory, Map<ProductVariantSize, Integer> quantities);

    Backlog getByFactoryId(String factoryId);

}