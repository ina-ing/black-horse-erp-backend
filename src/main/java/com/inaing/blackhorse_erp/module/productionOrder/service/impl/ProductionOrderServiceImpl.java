package com.inaing.blackhorse_erp.module.productionOrder.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.domain.enums.CodeType;
import com.inaing.blackhorse_erp.module.productionOrder.domain.ProductionOrder;
import com.inaing.blackhorse_erp.module.productionOrder.repository.ProductionOrderRepository;
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
}
