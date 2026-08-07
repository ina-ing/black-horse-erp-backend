package com.inaing.blackhorse_erp.module.productionOrder.service;

import java.util.List;

import com.inaing.blackhorse_erp.module.productionOrder.domain.ProductionOrder;

public interface IProductionOrderService {

    ProductionOrder create(ProductionOrder order);

    ProductionOrder update(ProductionOrder order);

    ProductionOrder getByIdentifier(String identifier);

    List<ProductionOrder> getAll();

    String generateProductionOrderCode();
}
