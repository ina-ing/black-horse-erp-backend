package com.inaing.blackhorse_erp.module.backlog.usecase.impl;

import org.springframework.stereotype.Component;

import com.inaing.blackhorse_erp.module.backlog.dto.response.BacklogResponseDto;
import com.inaing.blackhorse_erp.module.backlog.usecase.IBacklogUsecases;
import com.inaing.blackhorse_erp.module.backlog.usecase.impl.usecases.GetBacklogUsecase;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BacklogUsecasesImpl implements IBacklogUsecases {

    private final GetBacklogUsecase getBacklogUsecase;

    @Override
    public BacklogResponseDto getByFactory(String factoryIdentifier) {
        return getBacklogUsecase.execute(factoryIdentifier);
    }
}
