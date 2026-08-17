package com.inaing.blackhorse_erp.module.productionOrder.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.inaing.blackhorse_erp.module.productionOrder.domain.ProductionOrder;
import com.inaing.blackhorse_erp.module.productionOrder.dto.projections.ProductionOrderAnalyticsProjection;
import com.inaing.blackhorse_erp.module.productionOrder.dto.request.ProductionOrderFilter;

public interface IProductionOrderService {

    ProductionOrder create(ProductionOrder order);

    ProductionOrder update(ProductionOrder order);

    ProductionOrder getByIdentifier(String identifier);

    List<ProductionOrder> getAll();

    Page<ProductionOrder> getProductionOrders(ProductionOrderFilter filter, Pageable pageable);

    List<ProductionOrderAnalyticsProjection> getAnalytics();

}
