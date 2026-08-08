package com.inaing.blackhorse_erp.module.productionOrder.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
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
        order.setCode(this.generateProductionOrderCode());
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
    public String generateProductionOrderCode() {
        for (int attempt = 0; attempt < 10; attempt++) {
            String code = CodeGeneratorUtil.generate("POR-", 7);

            if (!productionOrderRepository.existsById(code)) {
                return code;
            }
        }
        throw new AppException(ErrorCode.IDENTIFIER_ALREADY_EXISTS);
    }

}
