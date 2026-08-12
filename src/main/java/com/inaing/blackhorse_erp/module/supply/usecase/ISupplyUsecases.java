package com.inaing.blackhorse_erp.module.supply.usecase;

import java.util.List;

import com.inaing.blackhorse_erp.module.supply.dto.request.SupplyCreationRequestDto;
import com.inaing.blackhorse_erp.module.supply.dto.request.SupplyItemsUpdateRequestDto;
import com.inaing.blackhorse_erp.module.supply.dto.request.SupplyStatusUpdateRequestDto;
import com.inaing.blackhorse_erp.module.supply.dto.response.SupplyResponseDto;

public interface ISupplyUsecases {

    SupplyResponseDto create(SupplyCreationRequestDto request);

    SupplyResponseDto getByIdentifier(String identifier);

    List<SupplyResponseDto> getAll();

    SupplyResponseDto updateStatus(String identifier, SupplyStatusUpdateRequestDto request);

    SupplyResponseDto updateItems(String identifier, SupplyItemsUpdateRequestDto request);
}
