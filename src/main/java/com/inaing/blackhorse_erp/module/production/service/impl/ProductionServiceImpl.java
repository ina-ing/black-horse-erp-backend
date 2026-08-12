package com.inaing.blackhorse_erp.module.production.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.domain.enums.CodeType;
import com.inaing.blackhorse_erp.module.production.domain.Production;
import com.inaing.blackhorse_erp.module.production.repository.ProductionRepository;
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
}
