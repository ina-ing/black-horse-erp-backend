package com.inaing.blackhorse_erp.module.production.service;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.inaing.blackhorse_erp.module.production.domain.Production;
import com.inaing.blackhorse_erp.module.production.dto.projections.ProductionTotalsProjection;
import com.inaing.blackhorse_erp.module.production.dto.request.ProductionFilter;

public interface IProductionService {

    Production create(Production production);

    Production update(Production production);

    Production getByIdentifier(String identifier);

    List<Production> getAll();

    List<Production> getByFactoryId(String factoryId);

    Page<Production> getProductions(ProductionFilter filter, Pageable pageable);

    ProductionTotalsProjection getTotals();

    long countSince(Instant from);
}
