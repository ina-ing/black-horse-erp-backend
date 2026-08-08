package com.inaing.blackhorse_erp.module.backlog.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.backlog.domain.Backlog;
import com.inaing.blackhorse_erp.module.backlog.repository.BacklogRepository;
import com.inaing.blackhorse_erp.module.backlog.service.IBacklogService;
import com.inaing.blackhorse_erp.module.factory.domain.Factory;
import com.inaing.blackhorse_erp.module.product.domain.ProductVariantSize;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BacklogServiceImpl implements IBacklogService {

    private final BacklogRepository backlogRepository;

    @Override
    @Transactional
    public Backlog create(Factory factory) {
        Backlog backlog = Backlog.builder()
                .factory(factory)
                .build();
        return backlogRepository.save(backlog);
    }

    @Override
    @Transactional
    public Backlog addQuantities(Factory factory, Map<ProductVariantSize, Integer> quantities) {
        Backlog backlog = backlogRepository.findByFactoryId(factory.getId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                        "Production backlog not found for factory " + factory.getId()));

        quantities.forEach(backlog::addQuantity);
        backlog.recalculateTotals();

        return backlogRepository.save(backlog);
    }

    @Override
    @Transactional(readOnly = true)
    public Backlog getByFactoryId(String factoryId) {
        return backlogRepository.findWithOutstandingItems(factoryId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                        "Production backlog not found for factory " + factoryId));
    }

}
