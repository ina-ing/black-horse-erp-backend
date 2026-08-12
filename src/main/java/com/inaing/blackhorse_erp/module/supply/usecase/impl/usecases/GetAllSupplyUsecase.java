package com.inaing.blackhorse_erp.module.supply.usecase.impl.usecases;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.module.supply.dto.response.SupplyResponseDto;
import com.inaing.blackhorse_erp.module.supply.mapper.SupplyMapper;
import com.inaing.blackhorse_erp.module.supply.service.ISupplyService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetAllSupplyUsecase {
    private final SupplyMapper supplyMapper;
    private final ISupplyService supplyService;

    @Transactional(readOnly = true)
    public List<SupplyResponseDto> execute() {
        return supplyService.getAll()
                .stream()
                .map(supplyMapper::toResponse)
                .toList();
    }
}
