package com.inaing.blackhorse_erp.module.backlog.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.backlog.dto.response.BacklogResponseDto;
import com.inaing.blackhorse_erp.module.backlog.mapper.BacklogMapper;
import com.inaing.blackhorse_erp.module.backlog.service.IBacklogService;
import com.inaing.blackhorse_erp.module.factory.domain.Factory;
import com.inaing.blackhorse_erp.module.factory.service.IFactoryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetBacklogUsecase {

    private final IFactoryService factoryService;
    private final IBacklogService backlogService;
    private final BacklogMapper backlogMapper;

    @Transactional(readOnly = true)
    public BacklogResponseDto execute(String factoryIdentifier) {

        Factory factory = factoryService.getByIdentifier(factoryIdentifier);
        if (factory == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "Factory not found " + factoryIdentifier);
        }

        return backlogMapper.toResponse(backlogService.getByFactoryId(factory.getId()));
    }
}
