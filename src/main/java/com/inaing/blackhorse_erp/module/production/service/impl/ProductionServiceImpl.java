package com.inaing.blackhorse_erp.module.production.service.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.domain.enums.CodeType;
import com.inaing.blackhorse_erp.module.production.domain.Production;
import com.inaing.blackhorse_erp.module.production.dto.projections.ProductionTotalsProjection;
import com.inaing.blackhorse_erp.module.production.dto.request.ProductionFilter;
import com.inaing.blackhorse_erp.module.production.repository.ProductionRepository;
import com.inaing.blackhorse_erp.module.production.repository.spec.ProductionSpecifications;
import com.inaing.blackhorse_erp.module.production.service.IProductionService;
import com.inaing.blackhorse_erp.utils.generators.CodeGeneratorUtil;
import com.inaing.blackhorse_erp.utils.uuid.UUIDUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductionServiceImpl implements IProductionService {

    private final ProductionRepository productionRepository;

    @Override
    @Transactional
    public Production create(Production production) {
        production.setCode(CodeGeneratorUtil.generateCode(CodeType.PRODUCTION));
        return productionRepository.save(production);
    }

    @Override
    @Transactional
    public Production update(Production production) {
        return productionRepository.save(production);
    }

    @Override
    @Transactional(readOnly = true)
    public Production getByIdentifier(String identifier) {
        if (UUIDUtils.isUUID(identifier)) {
            return productionRepository.findById(identifier).orElse(null);
        }
        return productionRepository.findByCode(identifier).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Production> getAll() {
        return productionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Production> getByFactoryId(String factoryId) {
        return productionRepository.findByFactoryId(factoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Production> getProductions(ProductionFilter filter, Pageable pageable) {
        Specification<Production> spec = Specification.allOf(
                ProductionSpecifications.producedBetween(filter.dateRange()),
                ProductionSpecifications.factoryIs(filter.factory()),
                ProductionSpecifications.matchesSearch(filter.search()));

        return productionRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductionTotalsProjection getTotals() {
        return productionRepository.findTotals();
    }

    @Override
    @Transactional(readOnly = true)
    public long countSince(Instant from) {
        return productionRepository.countByProductionDateGreaterThanEqual(from);
    }
}
