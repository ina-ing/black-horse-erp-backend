package com.inaing.blackhorse_erp.module.supply.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.supply.domain.Supply;
import com.inaing.blackhorse_erp.module.supply.dto.response.SupplyResponseDto;
import com.inaing.blackhorse_erp.module.supply.mapper.SupplyMapper;
import com.inaing.blackhorse_erp.module.supply.service.ISupplyService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetSupplyUsecase {

    private final SupplyMapper supplyMapper;
    private final ISupplyService supplyService;

    @Transactional(readOnly = true)
    public SupplyResponseDto execute(String identifier) {

        Supply supply = supplyService.getByIdentifier(identifier);
        if (supply == null) {
            throw new AppException(ErrorCode.SUPPLY_NOT_FOUND);
        }
        return supplyMapper.toResponse(supply);
    }
}
