package com.inaing.blackhorse_erp.module.supply.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.exception.exceptions.BusinessRuleException;
import com.inaing.blackhorse_erp.module.supply.domain.Supply;
import com.inaing.blackhorse_erp.module.supply.domain.enums.SupplyStatus;
import com.inaing.blackhorse_erp.module.supply.dto.request.SupplyStatusUpdateRequestDto;
import com.inaing.blackhorse_erp.module.supply.dto.response.SupplyResponseDto;
import com.inaing.blackhorse_erp.module.supply.mapper.SupplyMapper;
import com.inaing.blackhorse_erp.module.supply.service.ISupplyService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateSupplyStatusUsecase {

    private final SupplyMapper supplyMapper;
    private final ISupplyService supplyService;

    @Transactional
    public SupplyResponseDto execute(String identifier, SupplyStatusUpdateRequestDto request) {

        Supply supply = supplyService.getByIdentifier(identifier);
        if (supply == null) {
            throw new AppException(ErrorCode.SUPPLY_NOT_FOUND);
        }

        if (supply.getStatus() != SupplyStatus.PENDING) {
            throw new BusinessRuleException(
                    "SUPPLY_NOT_UPDATABLE",
                    "Only pending supplies can be accepted or flagged as issue.");
        }

        switch (request.status()) {
            case ACCEPTED -> accept(supply);
            case ISSUE -> issue(supply);
            default -> throw new BusinessRuleException(
                    "INVALID_STATUS_TRANSITION",
                    "Supply status can only be updated to accepted or issue.");
        }

        return supplyMapper.toResponse(supplyService.update(supply));
    }

    private void accept(Supply supply) {
        supply.setStatus(SupplyStatus.ACCEPTED);
        // TODO: increment warehouse inventory for each supply item once inventory module is wired in.
    }

    private void issue(Supply supply) {
        supply.setStatus(SupplyStatus.ISSUE);
        // TODO: notify admin once notification module is available.
    }
}
