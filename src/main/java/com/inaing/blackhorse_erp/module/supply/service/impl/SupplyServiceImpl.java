package com.inaing.blackhorse_erp.module.supply.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.domain.enums.CodeType;
import com.inaing.blackhorse_erp.module.supply.domain.Supply;
import com.inaing.blackhorse_erp.module.supply.repository.SupplyRepository;
import com.inaing.blackhorse_erp.module.supply.service.ISupplyService;
import com.inaing.blackhorse_erp.utils.generators.CodeGeneratorUtil;
import com.inaing.blackhorse_erp.utils.uuid.UUIDUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplyServiceImpl implements ISupplyService {

    private final SupplyRepository supplyRepository;

    @Override
    @Transactional
    public Supply create(Supply supply) {
        supply.setCode(CodeGeneratorUtil.generateCode(CodeType.SUPPLY));
        return supplyRepository.save(supply);
    }

    @Override
    @Transactional
    public Supply update(Supply supply) {
        return supplyRepository.save(supply);
    }

    @Override
    @Transactional(readOnly = true)
    public Supply getByIdentifier(String identifier) {
        if (UUIDUtils.isUUID(identifier)) {
            return supplyRepository.findById(identifier).orElse(null);
        }
        return supplyRepository.findByCode(identifier).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Supply> getAll() {
        return supplyRepository.findAll();
    }

}
