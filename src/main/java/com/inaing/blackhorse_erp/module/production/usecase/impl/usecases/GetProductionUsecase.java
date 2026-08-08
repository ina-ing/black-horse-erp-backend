package com.inaing.blackhorse_erp.module.production.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.production.domain.Production;
import com.inaing.blackhorse_erp.module.production.dto.response.ProductionResponseDto;
import com.inaing.blackhorse_erp.module.production.mapper.ProductionMapper;
import com.inaing.blackhorse_erp.module.production.service.IProductionService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetProductionUsecase {

    private final ProductionMapper productionMapper;
    private final IProductionService productionService;

    @Transactional(readOnly = true)
    public ProductionResponseDto execute(String identifier) {

        Production production = productionService.getByIdentifier(identifier);
        if (production == null) {
            throw new AppException(ErrorCode.PRODUCTION_NOT_FOUND, "Production not found " + identifier);
        }
        return productionMapper.toResponse(production);
    }
}
