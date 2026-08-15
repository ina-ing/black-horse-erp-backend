package com.inaing.blackhorse_erp.module.factory.usecase;

import com.inaing.blackhorse_erp.module.factory.dto.request.FactoryRequestDto;
import com.inaing.blackhorse_erp.module.factory.dto.request.FactoryUpdateRequestDto;
import com.inaing.blackhorse_erp.module.factory.dto.response.FactoryResponseDto;

public interface IFactoryUsecases {

    FactoryResponseDto create(FactoryRequestDto request);

    FactoryResponseDto update(String identifier, FactoryUpdateRequestDto request);

    FactoryResponseDto getByIdentifier(String identifier);

    FactoryResponseDto getOneFactory();
}
