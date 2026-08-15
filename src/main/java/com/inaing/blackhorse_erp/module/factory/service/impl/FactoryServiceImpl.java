package com.inaing.blackhorse_erp.module.factory.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.domain.enums.CodeType;
import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.factory.domain.Factory;
import com.inaing.blackhorse_erp.module.factory.repository.FactoryRepository;
import com.inaing.blackhorse_erp.module.factory.service.IFactoryService;
import com.inaing.blackhorse_erp.utils.generators.CodeGeneratorUtil;
import com.inaing.blackhorse_erp.utils.uuid.UUIDUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FactoryServiceImpl implements IFactoryService {

    private final FactoryRepository factoryRepository;

    @Override
    @Transactional
    public Factory create(Factory factory) {
        if (factoryRepository.existsByName(factory.getName())) {
            throw new AppException(ErrorCode.DUPLICATE_RESOURCE,
                    "Factory name already exists" + factory.getName());
        }
        factory.setCode(CodeGeneratorUtil.generateCode(CodeType.FACTORY));
        return factoryRepository.save(factory);
    }

    @Override
    @Transactional
    public Factory update(Factory factory) {
        factoryRepository.findByName(factory.getName())
                .filter(existing -> !existing.getId().equals(factory.getId()))
                .ifPresent(existing -> {
                    throw new AppException(ErrorCode.DUPLICATE_RESOURCE,
                            "Factory name already exists " + factory.getName());
                });
        return factoryRepository.save(factory);
    }

    @Override
    @Transactional(readOnly = true)
    public Factory getByIdentifier(String identifier) {
        if (UUIDUtils.isUUID(identifier)) {
            return factoryRepository.findById(identifier).orElse(null);
        }
        return factoryRepository.findByCode(identifier).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Factory getByManagerId(String managerId) {
        return factoryRepository.findByManagerId(managerId).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Factory getFactory() {
        List<Factory> factories = factoryRepository.findAll();
        if (factories.isEmpty()) {
            return null;
        }
        if (factories.size() != 1) {
            throw new AppException(ErrorCode.MULTIPLE_FACTORIES_NOT_SUPPORTED,
                    "Expected exactly one factory, found: " + factories.size());
        }
        return factories.get(0);
    }

}
