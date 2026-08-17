package com.inaing.blackhorse_erp.module.productionOrder.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.domain.enums.CodeType;
import com.inaing.blackhorse_erp.module.productionOrder.domain.ProductionOrder;
import com.inaing.blackhorse_erp.module.productionOrder.dto.projections.ProductionOrderAnalyticsProjection;
import com.inaing.blackhorse_erp.module.productionOrder.dto.request.ProductionOrderFilter;
import com.inaing.blackhorse_erp.module.productionOrder.repository.ProductionOrderRepository;
import com.inaing.blackhorse_erp.module.productionOrder.repository.spec.ProductionOrderSpecifications;
import com.inaing.blackhorse_erp.module.productionOrder.service.IProductionOrderService;
import com.inaing.blackhorse_erp.utils.generators.CodeGeneratorUtil;
import com.inaing.blackhorse_erp.utils.uuid.UUIDUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductionOrderServiceImpl implements IProductionOrderService {

    private final ProductionOrderRepository productionOrderRepository;

    @Override
    @Transactional
    public ProductionOrder create(ProductionOrder order) {
        order.setCode(CodeGeneratorUtil.generateCode(CodeType.PRODUCTION_ORDER));
        return productionOrderRepository.save(order);
    }

    @Override
    @Transactional
    public ProductionOrder update(ProductionOrder order) {
        return productionOrderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductionOrder getByIdentifier(String identifier) {
        if (UUIDUtils.isUUID(identifier)) {
            return productionOrderRepository.findById(identifier).orElse(null);
        }
        return productionOrderRepository.findByCode(identifier).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductionOrder> getAll() {
        return productionOrderRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductionOrder> getProductionOrders(ProductionOrderFilter filter, Pageable pageable) {
        Specification<ProductionOrder> spec = Specification.allOf(
                ProductionOrderSpecifications.orderDateBetween(filter.dateRange()),
                ProductionOrderSpecifications.statusIn(filter.statuses()),
                ProductionOrderSpecifications.matchesSearch(filter.search()));

        return productionOrderRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductionOrderAnalyticsProjection> getAnalytics() {
        return productionOrderRepository.findAnalytics();
    }
}
